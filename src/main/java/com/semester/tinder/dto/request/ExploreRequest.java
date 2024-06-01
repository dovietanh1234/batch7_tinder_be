package com.semester.tinder.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExploreRequest {
    private int userId;
    private double latitude;
    private double longitude;
    private int distance_preference;
    private String looking_for;
    private int min_age_preference;
    private int max_age_preference;
    private int offset;
}
