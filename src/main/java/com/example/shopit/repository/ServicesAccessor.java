package com.example.shopit.repository;

import com.example.shopit.service.AuthService;
import com.example.shopit.service.CartService;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Service
public class ServicesAccessor {
    @Getter private static AuthService authService;
    @Getter private static CartService cartService;

    ServicesAccessor(AuthService authService,
                     CartService cartService) {
        ServicesAccessor.authService = authService;
        ServicesAccessor.cartService = cartService;
    }
}
