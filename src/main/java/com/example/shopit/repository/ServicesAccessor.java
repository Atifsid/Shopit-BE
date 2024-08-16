package com.example.shopit.repository;

import com.example.shopit.service.AuthService;
import com.example.shopit.service.CartService;
import com.example.shopit.service.ProductService;
import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class ServicesAccessor {
    @Getter private static AuthService authService;
    @Getter private static CartService cartService;
    @Getter private static ModelMapper modelMapper;
    @Getter private static ProductService productService;

    ServicesAccessor(AuthService authService,
                     CartService cartService,
                     ModelMapper modelMapper,
                     ProductService productService) {
        ServicesAccessor.authService = authService;
        ServicesAccessor.cartService = cartService;
        ServicesAccessor.modelMapper = modelMapper;
        ServicesAccessor.productService = productService;
    }
}
