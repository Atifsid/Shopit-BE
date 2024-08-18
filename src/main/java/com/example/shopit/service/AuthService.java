package com.example.shopit.service;

import com.example.shopit.constant.Constant;
import com.example.shopit.dto.request.UserRequest;
import com.example.shopit.dto.response.ResponseDto;
import com.example.shopit.dto.response.UserResponse;
import com.example.shopit.entity.Product;
import com.example.shopit.entity.User;
import com.example.shopit.repository.RepositoryAccessor;
import com.example.shopit.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthService implements UserDetailsService {
    @Autowired
    private PasswordEncoder bcryptEncoder;
    @Autowired private JwtUtil jwtUtil;


    public void seedProducts() {
        Constant.PRODUCTS.forEach(product -> {
            Product newProduct = Product.builder()
                    .title(product.getTitle())
                    .price(product.getPrice())
                    .image(product.getImage())
                    .build();
            newProduct.setActive(true);
            newProduct.setDeleted(false);
            newProduct.setId(product.getId());
            RepositoryAccessor.getProductRepository().save(newProduct);
        });
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user =
                RepositoryAccessor.getUserRepository()
                        .findByEmailAndIsActiveAndIsDeleted(username, true, false)
                        .orElseThrow(
                                () ->
                                        new UsernameNotFoundException(
                                                String.format("User with email - %s, not found", username)));
        List<GrantedAuthority> grantedAuthorities = List.of(new SimpleGrantedAuthority("ADMIN"));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(), grantedAuthorities);
    }

    public ResponseDto<UserResponse> signup(UserRequest userRequest) {
        ResponseDto<UserResponse> response = new ResponseDto<>();
        Boolean isUserExistsByEmail = RepositoryAccessor.getUserRepository()
                .existsByEmailAndIsActiveAndIsDeleted(userRequest.getEmail(), true, false);
        if (isUserExistsByEmail) {
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage("Email already exists");
            response.setStatus(HttpStatus.BAD_REQUEST);
        } else {
            User user = User.builder()
                    .email(userRequest.getEmail())
                    .password(bcryptEncoder.encode(userRequest.getPassword()))
                    .build();
            RepositoryAccessor.getUserRepository().save(user);

            UserResponse userResponse =
                    UserResponse.builder()
                            .id(user.getId())
                            .email(user.getEmail())
                            .build();
            String authToken = jwtUtil.generateToken(userResponse);
            userResponse.setToken(authToken);
            response.setData(userResponse);
            response.setCode(HttpStatus.OK.value());
            response.setMessage("Sign up successful");
            response.setStatus(HttpStatus.OK);
        }
        return response;
    }

    public ResponseDto<UserResponse> login(UserRequest userRequest) {
        ResponseDto<UserResponse> response = new ResponseDto<>();
        Optional<User> user =
                RepositoryAccessor.getUserRepository()
                        .findByEmailIgnoreCaseAndIsActiveAndIsDeleted(userRequest.getEmail(), true, false);
        if (user.isPresent()) {
            boolean isLoggedIn =
                    bcryptEncoder.matches(userRequest.getPassword(), user.get().getPassword());
            if (!isLoggedIn) {
                response.setCode(HttpStatus.BAD_REQUEST.value());
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setMessage("Invalid password");
            } else {
                UserResponse userResponse = generateUserDetails(user.get());
                response.setCode(HttpStatus.OK.value());
                response.setStatus(HttpStatus.OK);
                response.setMessage("Login Successful");
                response.setData(userResponse);
            }
        } else {
            response.setCode(HttpStatus.BAD_REQUEST.value());
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setMessage("User does not exist");
        }

        return response;
    }

    public UserResponse generateUserDetails(User user) {
        UserResponse userDetailResponse =
                UserResponse.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .build();
        String authToken = jwtUtil.generateToken(userDetailResponse);
        userDetailResponse.setToken(authToken);
        return userDetailResponse;
    }
}
