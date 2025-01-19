package com.example.dao;

import com.example.entity.UserDO;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Author:Niu
 * Date:2025/1/19 16:06
 * Description: nothing.
 */
public interface UserRepository extends JpaRepository<UserDO, Long> {
}
