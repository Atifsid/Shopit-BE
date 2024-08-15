package com.example.shopit.controller;

import com.example.shopit.dto.request.UserRequest;
import com.example.shopit.dto.response.ProductResponse;
import com.example.shopit.dto.response.ResponseDto;
import com.example.shopit.dto.response.UserResponse;
import com.example.shopit.repository.ServicesAccessor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("cart")
public class CartController {
    @PutMapping("add/item/{id}")
    public ResponseDto<UserResponse> addItemToCart(@PathVariable Long id) {
        return ServicesAccessor.getCartService().addItemToCart(id);
    }

    @GetMapping("get/items")
    public ResponseDto<List<ProductResponse>> getUserItems() {
        return ServicesAccessor.getCartService().getUserItems();
    }
}
