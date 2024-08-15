package com.example.shopit.repository;

import com.example.shopit.service.AuthService;
import com.example.shopit.service.CartService;
import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class ServicesAccessor {
    @Getter private static AuthService authService;
    @Getter private static CartService cartService;
    @Getter private static ModelMapper modelMapper;

    ServicesAccessor(AuthService authService,
                     CartService cartService,
                     ModelMapper modelMapper) {
        ServicesAccessor.authService = authService;
        ServicesAccessor.cartService = cartService;
        ServicesAccessor.modelMapper = modelMapper;
    }
}
