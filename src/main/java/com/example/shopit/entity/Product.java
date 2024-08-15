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

    public Product(int i, String s, double v, String url) {
        this.title = s;
        this.image = url;
        this.price = (long) v;
        this.setId((long) i);
    }
}
