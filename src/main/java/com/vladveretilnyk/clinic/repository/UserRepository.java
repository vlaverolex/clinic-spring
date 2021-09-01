package com.vladveretilnyk.clinic.repository;

import com.vladveretilnyk.clinic.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
