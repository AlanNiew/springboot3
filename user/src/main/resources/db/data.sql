MERGE INTO users (name,password, age)
key (name)
VALUES
     ('John Doe', '123456', 30),
     ('Jane Smith', 'abcdef', 28),
     ('Mike Brown', 'qwerty', 35);

-- MERGE INTO 如果 name 不存在，则插入数据, 如果存在，则跳过
