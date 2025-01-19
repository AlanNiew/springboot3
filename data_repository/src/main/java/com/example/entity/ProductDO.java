package com.example.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Author:Niu
 * Date:2025/1/19 16:55
 * Description: nothing.
 */
@Data
@Entity
@Table(name = "product")
public class ProductDO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private BigDecimal price;
}
