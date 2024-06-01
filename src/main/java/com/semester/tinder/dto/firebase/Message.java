package com.semester.tinder.dto.firebase;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {

private String document_id = UUID.randomUUID().toString();

private String image;
private String content;
private int sender_id;
private int receive_id;
private int matches_id;


private Date sent_time = new Date() ;
private Date seen_time = new Date();
private String status = StatusEnum.SENT.getName();

}
