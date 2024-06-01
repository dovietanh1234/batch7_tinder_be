package com.semester.tinder.dto.request.RabbitMQ;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true) // sd o muc do class -> muc dich: bo qua cac thuoc tinh ko xac dinh khi dang deserialize data JSON, dat "ignoreUnknown = true" cac field se bo qua ko gay loi
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Notification {

    private String id;

    private int senderId;

    private int receiverId;

    private int idMatching2;

    private int codeNotify;

    private String title;

    private String content;

    private boolean isRead;

    private String urlAttached;

    private String image;

    private Date timeNotify;

}
