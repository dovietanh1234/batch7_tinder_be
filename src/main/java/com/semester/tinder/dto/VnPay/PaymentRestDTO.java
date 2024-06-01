package com.semester.tinder.dto.VnPay;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRestDTO implements Serializable {  // chuyen doi tuong thanh mot chuoi byte "serialize" de luu vao 1 file ...

    private String status;
    private String message;
    private String URL;

}
