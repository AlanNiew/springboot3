-- 如果表不存在，则创建商品表
CREATE TABLE IF NOT EXISTS product (
   id BIGINT AUTO_INCREMENT PRIMARY KEY, -- 商品ID，自动递增
   name VARCHAR(255) NOT NULL,           -- 商品名称，必填
   description VARCHAR(255),             -- 商品描述，非必填
   price DECIMAL(10, 2) NOT NULL         -- 商品价格，保留两位小数
);

