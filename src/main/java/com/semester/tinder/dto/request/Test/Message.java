package com.semester.tinder.dto.request.Test;

import com.semester.tinder.dto.firebase.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Message {
    private int matchId;
    private String message;
    private String nameSender;
    private int receiverId;
    private int senderId;
    private StatusEnum status;
    private Date timeSent;
    private String id;

}
