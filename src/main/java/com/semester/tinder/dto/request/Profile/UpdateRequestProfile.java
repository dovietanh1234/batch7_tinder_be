package com.semester.tinder.dto.request.Profile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRequestProfile {
    private int id;

    private String fullname;

    private String phone_number;

    private String gender;

    private Double latitude;

    private Double longitude;

}
