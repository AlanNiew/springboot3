/*
package com.example.service;

import com.example.domain.Product;
import com.example.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    // 新增或更新
    public Product save(Product product) {
        return productRepository.save(product);
    }

    // 根据 ID 查询
    public Optional<Product> findById(String id) {
        return productRepository.findById(id);
    }

    // 根据名称查询
    public List<Product> findByName(String name) {
        return productRepository.findByName(name);
    }

    // 根据分类查询
    public List<Product> findByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    // 删除
    public void deleteById(String id) {
        productRepository.deleteById(id);
    }
}
*/
