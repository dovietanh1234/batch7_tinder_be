package com.semester.tinder.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity(name = "explore")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Explore {
    @Id
    private int id;
    private String fullname;
    private String bio;
    private String relationship_goals;
    private String interests;
    private String height;
    private String languages;
    private int age;
    private Date date_birth;
    private String about_me;
    private String passions;
    private String images;
    private String life_style;
    private String basic;
    private String location;
    private Double latitude;
    private Double longitude;
}
