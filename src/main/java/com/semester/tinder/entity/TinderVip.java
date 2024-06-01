package com.semester.tinder.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "tinderVip") // sai cach dat ten
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TinderVip {
    @Id
    private int id;
    private String name;
    private double price;
    private String description;
    private String benefits;
}
