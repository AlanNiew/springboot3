package com.example.service.impl;

import com.example.dao.ProductRepository;
import com.example.entity.ProductDO;
import com.example.service.ProductService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Author:Niu
 * Date:2025/1/19 17:04
 * Description: nothing.
 */
@Service
public class ProductServiceImpl implements ProductService {
    @Resource
    private ProductRepository productRepository;

    @Override
    public List<ProductDO> list() {
        return productRepository.findAll();
    }
}
