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
    public ResponseDto<String> addItemToCart(@PathVariable Long id) {
        return ServicesAccessor.getCartService().addItemToCart(id);
    }

    @GetMapping("get/items")
    public ResponseDto<List<ProductResponse>> getUserItems() {
        return ServicesAccessor.getCartService().getUserItems();
    }

    @DeleteMapping("delete/item/{id}")
    public ResponseDto<String> deleteItemFromCart(@PathVariable Long id) {
        return ServicesAccessor.getCartService().deleteItemFromCart(id);
    }

    @PutMapping("decrease/item/{id}")
    public ResponseDto<String> decreaseItemInCart(@PathVariable Long id) {
        return ServicesAccessor.getCartService().decreaseItemInCart(id);
    }
}
