package com.semester.tinder.dto.request.Profile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileUpdate {

    private int id;

    private String bio;

    private String relationship_goals;

    private String interests;

    private String height;

    private String languages;

    private int age;

    private Date date_birth;

    private String passions;

    private String about_me;

    private String life_style;
    private String basic;
}