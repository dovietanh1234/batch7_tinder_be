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
public class EmailNotify {
private String nameUser;
   private String orderId;
   private String bankName;
   private String packageName;
   private String email;
   private double amount;
   private int codeNotify;
   private Date datePayment;
   private String phone;
}
