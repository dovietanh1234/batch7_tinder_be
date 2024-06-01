package com.semester.tinder.dto.request.lifestyle;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.semester.tinder.entity.User;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LifestyleRequest {
    private int id;
    private int user_id;

    private String pet;
    private String drinking;
    private String smoking;
    private String workout;
    private String dietary_preference;
    private String social_media;
    private String sleeping_habits;
}
