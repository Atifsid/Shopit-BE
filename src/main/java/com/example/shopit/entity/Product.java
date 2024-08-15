package com.example.shopit.entity;

import jakarta.persistence.Entity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Product extends BaseModel {
    private String title;
    private Long price;
    private String image;
}
