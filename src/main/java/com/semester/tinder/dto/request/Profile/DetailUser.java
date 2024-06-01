package com.semester.tinder.dto.request.Profile;

import lombok.Data;

@Data
public class DetailUser {
    private int id;
    private String full_name;

    private String email;
    private String phone_number;
    private String images;
}
