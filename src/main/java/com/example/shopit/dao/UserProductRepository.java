package com.example.shopit.dao;

import com.example.shopit.entity.UserProduct;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserProductRepository extends CrudRepository<UserProduct, Long> {
    Optional<UserProduct> findByUserIdAndProductIdAndIsActiveAndIsDeleted(Long userId, Long productId, Boolean isActive, Boolean isDeleted);
    List<UserProduct> findByUserIdAndAndIsActiveAndIsDeleted(Long productId, Boolean isActive, Boolean isDeleted);
}
