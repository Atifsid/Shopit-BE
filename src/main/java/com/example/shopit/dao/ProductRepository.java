package com.example.shopit.dao;

import com.example.shopit.entity.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product, Long> {
    List<Product> findByIsActiveAndIsDeleted(Boolean isActive, Boolean isDeleted);
}
