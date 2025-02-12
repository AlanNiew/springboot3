package com.example.controller;

import com.example.domain.Product;
import com.example.service.ElasticsearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class ElasticsearchController {

    private final ElasticsearchService elasticsearchService;

    @PostMapping
    public String addProduct(@RequestBody Product product) throws IOException {
        return elasticsearchService.addProduct(product);
    }

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable String id) throws IOException {
        return elasticsearchService.getProduct(id);
    }

    @PutMapping
    public String updateProduct(@RequestBody Product product) throws IOException {
        return elasticsearchService.updateProduct(product);
    }

    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable String id) throws IOException {
        return elasticsearchService.deleteProduct(id);
    }
}
