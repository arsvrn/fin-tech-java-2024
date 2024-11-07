package com.tbank.edu.hw13.repositoy;

import com.tbank.edu.hw13.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}

