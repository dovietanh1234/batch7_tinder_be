package com.semester.tinder.dto.request.Follower;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LikeUserRequest {
    private int u_id1;
    private int u_id2;
    private String status;
}
