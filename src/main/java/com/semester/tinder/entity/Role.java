package com.semester.tinder.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity(name = "role")
public class Role {
    @Id
    private int id;

    private String role_name;

}
