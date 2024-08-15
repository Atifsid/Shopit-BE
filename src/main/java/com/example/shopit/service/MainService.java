package com.example.shopit.service;

import com.example.shopit.constant.Constant;
import com.example.shopit.entity.Product;
import com.example.shopit.entity.User;
import com.example.shopit.repository.RepositoryAccessor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

// There could've been multiple services but this is a small project.
@Service
public class MainService implements UserDetailsService {

    public void seedProducts() {
        Constant.PRODUCTS.forEach(product -> {
            Product newProduct = Product.builder()
                    .title(product.getTitle())
                    .price(product.getPrice())
                    .image(product.getImage())
                    .build();
            newProduct.setActive(true);
            newProduct.setDeleted(false);
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
}
