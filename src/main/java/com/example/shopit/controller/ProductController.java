package com.example.shopit.controller;

import com.example.shopit.dto.response.ProductResponse;
import com.example.shopit.dto.response.ResponseDto;
import com.example.shopit.repository.ServicesAccessor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("products")
public class ProductController {
    @GetMapping("get")
    public ResponseDto<List<ProductResponse>> getProducts() {
        return ServicesAccessor.getProductService().getProducts();
    }
}
