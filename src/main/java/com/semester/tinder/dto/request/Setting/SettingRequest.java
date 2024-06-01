package com.semester.tinder.dto.request.Setting;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SettingRequest {
        private int id;

        private int user_id;

        private String location;

        private int distance_preference;

        private String looking_for;

        private int min_age_preference;
        private int max_age_preference;

}
