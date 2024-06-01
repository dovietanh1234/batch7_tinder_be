package com.semester.tinder.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity(name = "follower")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Follower {

    @Id
    private int id;

    private String status;

    private Date match_at_time;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "u_id1")
    private User implementer; // nguoi thuc hien

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "u_id2")
    private User affected_person; // nguoi bi anh huong

}
