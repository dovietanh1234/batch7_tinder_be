package com.semester.tinder.dto.request.Follower;

import lombok.Data;

@Data
public class DetailFollowerDTO {
    private int u_id1;
    private int u_id2;
    private String status = "Matching";
    private int offSet = 0;
}
