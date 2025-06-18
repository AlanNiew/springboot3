package com.example.proxy.mybaits.factory;

import com.example.proxy.mybaits.annontion.Param;
import com.example.proxy.mybaits.annontion.Table;
import com.example.proxy.mybaits.entity.User;

import java.lang.reflect.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @Author: Niu
 * @Date: 2025/5/22 14:20
 * @Description:
 */
public class MySqlQueryFactory {


    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/test?useSSL=false";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "123456";

    public <T> T getMapper(Class<T> clazz) {
        return clazz.cast(Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{clazz}, new MapperInvocationHandler()));
    }

    static class MapperInvocationHandler implements InvocationHandler {
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String name = method.getName();
            if (name.startsWith("query") || name.startsWith("select")){
                return selectInvoke(method,args);
            } else if (name.startsWith("insert")) {
                return insertInvoke(method,args);
            }
            throw new RuntimeException("不支持的方法");
        }

        public Object selectInvoke(Method method, Object[] args) throws Throwable {
            String sql = generateSelectSql(method);
            System.out.println(sql);
            try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
                 PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                for (int i = 0; i < args.length; i++) {
                    Object arg = args[i];
                    if (arg instanceof Integer) {
                        preparedStatement.setInt(i+1, (Integer) arg);
                    }else if (arg instanceof String){
                        preparedStatement.setString(i+1, arg.toString());
                    }else {
                        throw new RuntimeException("不支持的类型");
                    }
                }
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    return parseResultToObject(resultSet, method.getReturnType());
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return null;
        }

        public Object insertInvoke(Method method, Object[] args) throws Throwable {
            String sql = generateInsertSql(method,args);
            System.out.println(sql);
            try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
                 PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                for (Object arg : args) {
                    if (arg instanceof User) {
                        preparedStatement.setString(1, ((User) arg).getName());
                        preparedStatement.setString(2, ((User) arg).getPassword());
                        preparedStatement.setString(3, ((User) arg).getEmail());
                    }
                }
                int i = preparedStatement.executeUpdate();
                return i > 0;
            }
        }

        /**
         * 生成查询SQL
         * @param method
         * @return
         */
        private String generateSelectSql(Method method) {
            return "select " +
                    createSelectFields(method) +
                    " from " +
                    method.getReturnType().getAnnotation(Table.class).tableName() +
                    " where " +
                    createSelectWhere(method);
        }

        /**
         * 插入sql
         *
         * @param method
         * @param args
         * @return
         */
        private String generateInsertSql(Method method, Object[] args) throws IllegalAccessException {
            Object arg = args[0];
            if (arg instanceof User) {
                User user = (User) arg;
                return "insert into " +
                        user.getClass().getAnnotation(Table.class).tableName() +
                        " (name,password,email) values (?,?,?)";
            }
            return "insert into " +
                    method.getReturnType().getAnnotation(Table.class).tableName() +
                    " values (?,?,?)";
        }

        private String createSelectFields(Method method) {
            return  Arrays.stream(method.getReturnType().getDeclaredFields())
                    .map(Field::getName).collect(Collectors.joining(","));
        }
        private String createSelectWhere(Method method) {
            return Arrays.stream(method.getParameters()).map(
                    param -> param.getAnnotation(Param.class).value() + " = ?")
                    .collect(Collectors.joining(" and "));
        }

        private Object parseResultToObject(ResultSet resultSet, Class<?> clazz) throws Exception {
            Field[] declaredFields = clazz.getDeclaredFields();
            Object obj = clazz.getConstructor().newInstance();
            for (Field field : declaredFields) {
                field.setAccessible(true);
                field.set(obj, resultSet.getObject(field.getName()));
            }
            return obj;
        }
    }
}
