package com.example.shopit.controller;

import com.example.shopit.dto.response.ProductResponse;
import com.example.shopit.dto.response.ResponseDto;
import com.example.shopit.repository.ServicesAccessor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("cart")
public class CartController {
    @PutMapping("add/item/{id}")
    public ResponseDto<ProductResponse> addItemToCart(@PathVariable Long id) {
        return ServicesAccessor.getCartService().addItemToCart(id);
    }

    @GetMapping("get/items")
    public ResponseDto<List<ProductResponse>> getUserItems() {
        return ServicesAccessor.getCartService().getUserItems();
    }

    @GetMapping("get/size")
    public ResponseDto<Long> getCartSize() {
        return ServicesAccessor.getCartService().getCartSize();
    }

    @DeleteMapping("delete/item/{id}")
    public ResponseDto<Long> deleteItemFromCart(@PathVariable Long id) {
        return ServicesAccessor.getCartService().deleteItemFromCart(id);
    }

    @PutMapping("decrease/item/{id}")
    public ResponseDto<ProductResponse> decreaseItemInCart(@PathVariable Long id) {
        return ServicesAccessor.getCartService().decreaseItemInCart(id);
    }
}
