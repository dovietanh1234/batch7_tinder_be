package com.semester.tinder.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // bỏ qua field nào là null
public class ApiResponse <T> {
    // class này sẽ contains all fields mà mình mong muốn normalize cho cai API cua minh:
    private int code;
    private String message;

    private T result; // generic type. kieu tra ve co the thay doi tuy thuoc vao tung API

}
