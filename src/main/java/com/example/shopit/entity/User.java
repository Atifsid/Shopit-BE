package com.example.shopit.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class User extends BaseModel {
    private String name;
    private String email;
    private String password;
    private String gender;
}