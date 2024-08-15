package com.example.shopit.dao;

import com.example.shopit.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByEmailAndIsActiveAndIsDeleted(String email, Boolean isActive, Boolean isDeleted);
    Optional<User> findByIdAndIsActiveAndIsDeleted(Long id, Boolean isActive, Boolean isDeleted);
    Boolean existsByEmailAndIsActiveAndIsDeleted(String email, Boolean isActive, Boolean isDeleted);
    Optional<User> findByEmailIgnoreCaseAndIsActiveAndIsDeleted(String email, Boolean isActive, Boolean isDeleted);
}
