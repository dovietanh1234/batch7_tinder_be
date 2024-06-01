package com.semester.tinder.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity(name = "vipUsers") // sai cach dat ten
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VipUsers {
    @Id
    private int id;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "u_id")
    private User user;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "vip_id")
    private TinderVip tinderVip;

    private LocalDateTime start_time;
    private LocalDateTime end_time;

}
