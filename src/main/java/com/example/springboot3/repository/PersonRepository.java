package com.example.springboot3.repository;

import com.example.springboot3.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PersonRepository extends JpaRepository<Person, Long> {
    // 基于方法名查询
    List<Person> findByName(String name);
}
