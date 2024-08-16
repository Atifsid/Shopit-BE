package com.example.shopit.service;

import com.example.shopit.dto.response.ProductResponse;
import com.example.shopit.dto.response.ResponseDto;
import com.example.shopit.entity.User;
import com.example.shopit.repository.RepositoryAccessor;
import com.example.shopit.utils.CommonUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    public ResponseDto<List<ProductResponse>> getProducts() {
        ResponseDto<List<ProductResponse>> response = new ResponseDto<>();

        Optional<User> optionalUser = CommonUtils.getLoggedInUser();
        if (optionalUser.isPresent()) {
            List<ProductResponse> productResponses = RepositoryAccessor.getProductRepository().findByIsActiveAndIsDeleted(true, false)
                    .stream().map(product -> ProductResponse.builder()
                            .id(product.getId())
                            .title(product.getTitle())
                            .image(product.getImage())
                            .price(product.getPrice())
                            .build()).toList();

            response.setCode(HttpStatus.OK.value());
            response.setStatus(HttpStatus.OK);
            response.setData(productResponses);
            response.setMessage("Successfully fetched products");
        } else {
            response.setCode(HttpStatus.UNAUTHORIZED.value());
            response.setStatus(HttpStatus.UNAUTHORIZED);
            response.setMessage("Unauthorized User");
        }


        return response;
    }
}
