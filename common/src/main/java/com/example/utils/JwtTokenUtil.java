package com.example.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class JwtTokenUtil {

    // 密钥：用于签名和验证JWT，应该保存在配置文件或者环境变量中
    private static final String SECRET_KEY = "your-secret-key"; // 密钥最好从配置文件读取
    private static final long EXPIRATION_TIME = 86400000L; // 1天过期时间（单位毫秒）

    /**
     * 生成JWT Token
     *
     * @param username 用户名
     * @return JWT Token
     */
    public static String generateToken(String username) {
        return Jwts.builder()
                .claims(initClaims(username))
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()),Jwts.SIG.HS256)
                .compact();
    }

    /**
     * 解析JWT Token，获取Claims（即存储的用户信息）
     *
     * @param token JWT Token
     * @return Claims
     * @throws UnsupportedJwtException JWT解析异常
     */
    public static Claims parseToken(String token) throws UnsupportedJwtException {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * 从JWT Token中获取用户名
     *
     * @param token JWT Token
     * @return 用户名
     */
    public static String getUsernameFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.getSubject(); // 获取JWT的主体（用户名）
    }

    /**
     * 验证JWT Token是否有效（是否过期和是否匹配用户名）
     *
     * @param token JWT Token
     * @param username 用户名
     * @return 是否有效
     */
    public static boolean validateToken(String token, String username) {
        String extractedUsername = getUsernameFromToken(token);
        return (username.equals(extractedUsername) && !isTokenExpired(token));
    }

    /**
     * 验证JWT是否过期
     *
     * @param token JWT Token
     * @return 是否过期
     */
    private static boolean isTokenExpired(String token) {
        Claims claims = parseToken(token);
        return claims.getExpiration().before(new Date());
    }

    /**
     * 刷新JWT Token（重新生成一个有效的JWT Token）
     *
     * @param oldToken 旧的JWT Token
     * @return 新的JWT Token
     */
    public static String refreshToken(String oldToken) {
        String username = getUsernameFromToken(oldToken);
        return generateToken(username);
    }


    /**
     * 初始化负载内数据
     * @param username 用户名
     * @return 负载集合
     */
    private static Map<String,Object> initClaims(String username){
        Map<String, Object> claims = new HashMap<>();
        //"iss" (Issuer): 代表 JWT 的签发者。在这个字段中填入一个字符串，表示该 JWT 是由谁签发的。例如，可以填入你的应用程序的名称或标识符。
        claims.put("iss","jx");
        //"sub" (Subject): 代表 JWT 的主题，即该 JWT 所面向的用户。可以是用户的唯一标识符或者其他相关信息。
        claims.put("sub",username);
        //"exp" (Expiration Time): 代表 JWT 的过期时间。通常以 UNIX 时间戳表示，表示在这个时间之后该 JWT 将会过期。建议设定一个未来的时间点以保证 JWT 的有效性，比如一个小时、一天、一个月后的时间。
        claims.put("exp",generatorExpirationDate());
        //"aud" (Audience): 代表 JWT 的接收者。这个字段可以填入该 JWT 预期的接收者，可以是单个用户、一组用户、或者某个服务。
        claims.put("aud","internal use");
        //"iat" (Issued At): 代表 JWT 的签发时间。同样使用 UNIX 时间戳表示。
        claims.put("iat",new Date());
        //"jti" (JWT ID): JWT 的唯一标识符。这个字段可以用来标识 JWT 的唯一性，避免重放攻击等问题。
        claims.put("jti", UUID.randomUUID().toString());
        //"nbf" (Not Before): 代表 JWT 的生效时间。在这个时间之前 JWT 不会生效，通常也是一个 UNIX 时间戳。我这里不填，没这个需求
        return claims;
    }

    /**
     * 生成失效时间，以秒为单位
     *
     * @return 预计失效时间
     */
    private static Date generatorExpirationDate()
    {
        //预计失效时间为：token生成时间+预设期间
        return new Date(System.currentTimeMillis() + EXPIRATION_TIME * 1000);
    }
}
