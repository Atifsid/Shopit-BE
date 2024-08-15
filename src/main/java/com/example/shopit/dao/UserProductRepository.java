package com.example.shopit.dao;

import com.example.shopit.entity.UserProduct;
import org.springframework.data.repository.CrudRepository;

public interface UserProductRepository extends CrudRepository<UserProduct, Long> {
}
