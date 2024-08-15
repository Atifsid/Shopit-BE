package com.example.shopit.repository;

import com.example.shopit.dao.ProductRepository;
import com.example.shopit.dao.UserProductRepository;
import com.example.shopit.dao.UserRepository;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Service
public class RepositoryAccessor {
    @Getter private static ProductRepository productRepository;
    @Getter private static UserRepository userRepository;
    @Getter private static UserProductRepository userProductRepository;

    RepositoryAccessor(ProductRepository productRepository,
                       UserRepository userRepository,
                       UserProductRepository userProductRepository) {
        RepositoryAccessor.productRepository = productRepository;
        RepositoryAccessor.userRepository = userRepository;
        RepositoryAccessor.userProductRepository = userProductRepository;
    }
}
