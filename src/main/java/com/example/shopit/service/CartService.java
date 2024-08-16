package com.example.shopit.service;

import com.example.shopit.dto.response.ProductResponse;
import com.example.shopit.dto.response.ResponseDto;
import com.example.shopit.dto.response.UserResponse;
import com.example.shopit.entity.Product;
import com.example.shopit.entity.User;
import com.example.shopit.entity.UserProduct;
import com.example.shopit.repository.RepositoryAccessor;
import com.example.shopit.utils.CommonUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {
    public ResponseDto<ProductResponse> addItemToCart(Long productId) {
        ResponseDto<ProductResponse> response = new ResponseDto<>();

        Optional<User> optionalUser = CommonUtils.getLoggedInUser();
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Optional<UserProduct> optionalUserProduct = RepositoryAccessor.getUserProductRepository().findByUserIdAndProductIdAndIsActiveAndIsDeleted(user.getId(), productId, true, false);
            if (optionalUserProduct.isPresent()) {
                UserProduct userProduct = optionalUserProduct.get();
                Integer oldQty = userProduct.getQuantity();
                userProduct.setQuantity(oldQty + 1);
                RepositoryAccessor.getUserProductRepository().save(userProduct);
                Product product = userProduct.getProduct();
                ProductResponse productResponse = ProductResponse.builder()
                        .id(product.getId())
                        .title(product.getTitle())
                        .image(product.getImage())
                        .price(product.getPrice())
                        .quantity(oldQty + 1)
                        .build();

                response.setCode(HttpStatus.OK.value());
                response.setStatus(HttpStatus.OK);
                response.setData(productResponse);
                response.setMessage("Item quantity increased in the cart");
            } else {
                Optional<Product> optionalProduct = RepositoryAccessor.getProductRepository().findById(productId);
                if (optionalProduct.isPresent()) {
                    Product product = optionalProduct.get();
                    UserProduct userProduct = UserProduct.builder()
                            .product(product)
                            .user(user)
                            .quantity(1)
                            .build();

                    ProductResponse productResponse = ProductResponse.builder()
                            .id(product.getId())
                            .title(product.getTitle())
                            .image(product.getImage())
                            .price(product.getPrice())
                            .quantity(1)
                            .build();
                    RepositoryAccessor.getUserProductRepository().save(userProduct);
                    response.setCode(HttpStatus.OK.value());
                    response.setStatus(HttpStatus.OK);
                    response.setData(productResponse);
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

        Optional<User> optionalUser = CommonUtils.getLoggedInUser();
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            List<ProductResponse> userProducts = RepositoryAccessor.getUserProductRepository().findByUserIdAndAndIsActiveAndIsDeleted(user.getId(), true, false)
                    .stream().map(userProduct -> ProductResponse.builder()
                            .id(userProduct.getProduct().getId())
                            .title(userProduct.getProduct().getTitle())
                            .image(userProduct.getProduct().getImage())
                            .quantity(userProduct.getQuantity())
                            .price(userProduct.getProduct().getPrice())
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

    public ResponseDto<Long> deleteItemFromCart(Long id) {
        ResponseDto<Long> response = new ResponseDto<>();

        Optional<User> optionalUser = CommonUtils.getLoggedInUser();
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Optional<UserProduct> optionalUserProduct = RepositoryAccessor.getUserProductRepository().findByUserIdAndProductIdAndIsActiveAndIsDeleted(user.getId(), id, true, false);
            if (optionalUserProduct.isPresent()) {
                UserProduct userProduct = optionalUserProduct.get();
                userProduct.setActive(false);
                userProduct.setDeleted(true);
                RepositoryAccessor.getUserProductRepository().save(userProduct);
                response.setCode(HttpStatus.OK.value());
                response.setStatus(HttpStatus.OK);
                response.setData(userProduct.getProduct().getId());
                response.setMessage("Item removed from cart");
            } else {
                response.setCode(HttpStatus.BAD_REQUEST.value());
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setMessage("Invalid Product id");
            }
        } else {
            response.setCode(HttpStatus.UNAUTHORIZED.value());
            response.setStatus(HttpStatus.UNAUTHORIZED);
            response.setMessage("Unauthorized User");
        }

        return response;
    }

    public ResponseDto<ProductResponse> decreaseItemInCart(Long id) {
        ResponseDto<ProductResponse> response = new ResponseDto<>();

        Optional<User> optionalUser = CommonUtils.getLoggedInUser();
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Optional<UserProduct> optionalUserProduct = RepositoryAccessor.getUserProductRepository().findByUserIdAndProductIdAndIsActiveAndIsDeleted(user.getId(), id, true, false);
            if(optionalUserProduct.isPresent()) {
                UserProduct userProduct = optionalUserProduct.get();
                Product product = userProduct.getProduct();
                ProductResponse productResponse = ProductResponse.builder()
                        .id(product.getId())
                        .title(product.getTitle())
                        .image(product.getImage())
                        .price(product.getPrice())
                        .build();
                if (userProduct.getQuantity().equals(1)) {
                    userProduct.setDeleted(true);
                    userProduct.setActive(false);
                    productResponse.setQuantity(0);
                    response.setMessage("Item removed from the cart");
                } else {
                    int oldQty = userProduct.getQuantity();
                    userProduct.setQuantity(oldQty - 1);
                    productResponse.setQuantity(oldQty - 1);
                    response.setMessage("Item quantity decreased in cart");
                }
                RepositoryAccessor.getUserProductRepository().save(userProduct);
                response.setCode(HttpStatus.OK.value());
                response.setData(productResponse);
                response.setStatus(HttpStatus.OK);
            } else {
                response.setCode(HttpStatus.BAD_REQUEST.value());
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setMessage("Item not present in cart");
            }
        } else {
            response.setCode(HttpStatus.UNAUTHORIZED.value());
            response.setStatus(HttpStatus.UNAUTHORIZED);
            response.setMessage("Unauthorized User");
        }

        return response;
    }

    public ResponseDto<Long> getCartSize() {
        ResponseDto<Long> response = new ResponseDto<>();

        Optional<User> optionalUser = CommonUtils.getLoggedInUser();
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Long count = RepositoryAccessor.getUserProductRepository().countByUserIdAndIsActiveAndIsDeleted(user.getId(), true, false);
            response.setCode(HttpStatus.OK.value());
            response.setStatus(HttpStatus.OK);
            response.setData(count);
        } else {
            response.setCode(HttpStatus.UNAUTHORIZED.value());
            response.setStatus(HttpStatus.UNAUTHORIZED);
            response.setMessage("Unauthorized User");
        }
        return response;
    }
}
