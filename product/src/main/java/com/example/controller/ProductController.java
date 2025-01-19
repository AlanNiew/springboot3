package com.example.controller;

import com.example.api.CommonResult;
import com.example.dao.ProductRepository;
import com.example.entity.ProductDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Author:Niu
 * Date:2025/1/19 17:06
 * Description: nothing.
 */
@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;
    @GetMapping("/list")
    public CommonResult<?> test3() {
        List<ProductDO> productDOList = productRepository.findAll();
        return CommonResult.success(productDOList, "查询成功");
    }
}
