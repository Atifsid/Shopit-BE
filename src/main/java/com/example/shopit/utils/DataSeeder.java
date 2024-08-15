package com.example.shopit.utils;

import com.example.shopit.repository.ServicesAccessor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner{
    @Override
    public void run(String... args) throws Exception {
        ServicesAccessor.getAuthService().seedProducts();
    }
}
