package com.semester.tinder.dto.request.Follower;

import com.semester.tinder.dto.request.Test.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailUserMatchingDTO {

    private String fullname;
    private String bio;

    private String relationship_goals;

    private String interests;

    private String height;

    private String languages;

    private int age;

    private Date date_birth;

    private String passions;

    private String about_me;

    private Date date_matching;

    private String life_style;
    private String basic;
    private String images;

    private List<Message> messages;

}
