package com.semester.tinder.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name="setting")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Setting {

    @Id
    private int id;

    private String location;

    private int distance_preference;

    private String looking_for;

    private int min_age_preference;
    private int max_age_preference;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "u_id")
    private User user;

}
