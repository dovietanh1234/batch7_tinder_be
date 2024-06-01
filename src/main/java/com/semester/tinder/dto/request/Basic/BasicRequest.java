package com.semester.tinder.dto.request.Basic;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.semester.tinder.entity.User;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BasicRequest {
    private int id;

    private int user_id;

    private String zodiac;

    private String education;

    private String family_plans;

    private String communication_style;

    private String personality_style;

    private String love_style;
}
