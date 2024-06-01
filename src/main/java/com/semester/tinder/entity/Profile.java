package com.semester.tinder.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity(name="profile")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Profile {

    @Id
    private int id;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "u_id")
    private User user;

    private String bio;

    private String relationship_goals;

    private String interests;

    private String height;

    private String languages;

    private int age;

    private Date date_birth;

    private String passions;

    private String about_me;

    @Column(name = "life_style")
    private String life_style;

    @Column(name = "basic")
    private String basic;

}
