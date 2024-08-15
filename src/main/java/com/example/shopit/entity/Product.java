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

    public Product(int id, String title, double price, String image) {
        this.title = title;
        this.image = image;
        this.price = (long) price;
        this.setId((long) id);
    }
}
