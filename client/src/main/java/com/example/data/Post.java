package com.example.data;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Post {
    private Long id;
    private Long userId;
    private String title;
    private String body;
}