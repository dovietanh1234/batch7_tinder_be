package com.semester.tinder.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity(name = "checkCountLikes") // sai cach dat ten
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckCountLikes {
    @Id
    private int id;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "u_id")
    private User user;

    private int likeCount;

    private LocalDateTime lastLikeTime;

}
