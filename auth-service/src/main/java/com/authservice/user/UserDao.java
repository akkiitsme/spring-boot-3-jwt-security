package com.authservice.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDao extends JpaRepository<User,Integer> {

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String username);
}
