package com.semester.tinder.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.semester.tinder.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true) // sd o muc do class -> muc dich: bo qua cac thuoc tinh ko xac dinh khi dang deserialize data JSON, dat "ignoreUnknown = true" cac field se bo qua ko gay loi
@JsonInclude(JsonInclude.Include.NON_NULL)//JsonInclude -> sd chi dinh cac quy tac bao gom cac gia tri thuoc tinh trong JSON Serialize,
// JsonInclude.Include.NON_NULL -> dam bao cac field ko null  chi lay cac field co data
public class ReqRes {
    private int Id_user;
    private int statusCode;
    private String error;
    private String message;
    private String token;
    private String refreshToken;
    private String expirationTime;
    private String name;
    private String email;
    private String role;
    private String password;
    private User ourUser;
    private Boolean isHasProfile;
}
