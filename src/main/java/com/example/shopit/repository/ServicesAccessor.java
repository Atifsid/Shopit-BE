package com.example.shopit.repository;

import com.example.shopit.service.MainService;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Service
public class ServicesAccessor {
    @Getter private static MainService mainService;

    ServicesAccessor(MainService mainService) {
        ServicesAccessor.mainService = mainService;
    }
}
