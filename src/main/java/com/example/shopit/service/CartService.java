package com.example.shopit.service;

import com.example.shopit.dto.response.ProductResponse;
import com.example.shopit.dto.response.ResponseDto;
import com.example.shopit.dto.response.UserResponse;
import com.example.shopit.entity.Product;
import com.example.shopit.entity.User;
import com.example.shopit.entity.UserProduct;
import com.example.shopit.repository.RepositoryAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {
    public ResponseDto<UserResponse> addItemToCart(Long productId) {
        ResponseDto<UserResponse> response = new ResponseDto<>();
        // Fetching details from token
        UserResponse userResponse =
                (UserResponse) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> optionalUser = RepositoryAccessor.getUserRepository().findByIdAndIsActiveAndIsDeleted(userResponse.getId(), true, false);

        if (optionalUser.isPresent()) {
            Optional<UserProduct> optionalUserProduct = RepositoryAccessor.getUserProductRepository().findByUserIdAndProductIdAndIsActiveAndIsDeleted(userResponse.getId(), productId, true, false);
            if (optionalUserProduct.isPresent()) {
                UserProduct userProduct = optionalUserProduct.get();
                userProduct.setQuantity(userProduct.getQuantity() + 1);
                RepositoryAccessor.getUserProductRepository().save(userProduct);
                response.setCode(HttpStatus.OK.value());
                response.setStatus(HttpStatus.OK);
                response.setMessage("Item quantity increased in the cart");
            } else {
                Optional<Product> optionalProduct = RepositoryAccessor.getProductRepository().findById(productId);
                if (optionalProduct.isPresent()) {
                    UserProduct userProduct = UserProduct.builder()
                            .product(optionalProduct.get())
                            .user(optionalUser.get())
                            .quantity(1)
                            .build();
                    RepositoryAccessor.getUserProductRepository().save(userProduct);
                    response.setCode(HttpStatus.OK.value());
                    response.setStatus(HttpStatus.OK);
                    response.setMessage("Item added to cart");
                } else {
                    response.setCode(HttpStatus.BAD_REQUEST.value());
                    response.setStatus(HttpStatus.BAD_REQUEST);
                    response.setMessage("Invalid Product id");
                }
            }
        } else {
            response.setCode(HttpStatus.UNAUTHORIZED.value());
            response.setStatus(HttpStatus.UNAUTHORIZED);
            response.setMessage("Unauthorized User");
        }

        return response;
    }

    public ResponseDto<List<ProductResponse>> getUserItems() {
        ResponseDto<List<ProductResponse>> response = new ResponseDto<>();
        // Fetching details from token
        UserResponse userResponse =
                (UserResponse) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> optionalUser = RepositoryAccessor.getUserRepository().findByIdAndIsActiveAndIsDeleted(userResponse.getId(), true, false);

        if (optionalUser.isPresent()) {
            List<ProductResponse> userProducts = RepositoryAccessor.getUserProductRepository().findByUserIdAndAndIsActiveAndIsDeleted(userResponse.getId(), true, false)
                    .stream().map(userProduct -> ProductResponse.builder()
                            .id(userProduct.getProduct().getId())
                            .title(userProduct.getProduct().getTitle())
                            .image(userProduct.getProduct().getImage())
                            .quantity(userProduct.getQuantity())
                            .build()).toList();
            response.setCode(HttpStatus.OK.value());
            response.setStatus(HttpStatus.OK);
            response.setData(userProducts);
            response.setMessage("Successfully fetched cart items");
        } else {
            response.setCode(HttpStatus.UNAUTHORIZED.value());
            response.setStatus(HttpStatus.UNAUTHORIZED);
            response.setMessage("Unauthorized User");
        }

        return response;
    }
}
