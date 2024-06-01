package com.semester.tinder.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity(name = "ufollower")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsersFollower {

    @Id
    private int id;
    private String fullname;
    private Date time_matching;
    private String images;
    private int id_matching;
    private String new_message;
}
