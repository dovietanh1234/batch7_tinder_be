package com.semester.tinder.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InformsProfile {

    private int idProfile;
    private String bio;

    private String relationship_goals;

    private String interests;

    private String height;

    private String languages;

    private int age;

    private Date date_birth;

    private String passions;

    private String about_me;

    private String images;

    private String fullname;

    private String email;

    private String phone_number;

    private String gender;


}
