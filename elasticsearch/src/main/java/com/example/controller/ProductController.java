/*
package com.example.controller;

import com.example.domain.Product;
import com.example.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    // 添加或更新产品
    @PostMapping
    public Product save(@RequestBody Product product) {
        return productService.save(product);
    }

    // 根据 ID 查询
    @GetMapping("/{id}")
    public Optional<Product> findById(@PathVariable String id) {
        return productService.findById(id);
    }

    // 根据名称查询
    @GetMapping("/name/{name}")
    public List<Product> findByName(@PathVariable String name) {
        return productService.findByName(name);
    }

    // 根据分类查询
    @GetMapping("/category/{category}")
    public List<Product> findByCategory(@PathVariable String category) {
        return productService.findByCategory(category);
    }

    // 删除
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        productService.deleteById(id);
    }
}
*/
