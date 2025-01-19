package com.example.dao;

import com.example.entity.ProductDO;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Author:Niu
 * Date:2025/1/19 17:01
 * Description: nothing.
 */
public interface ProductRepository extends JpaRepository<ProductDO, Long> {
}
