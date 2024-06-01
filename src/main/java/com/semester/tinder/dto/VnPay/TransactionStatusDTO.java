package com.semester.tinder.dto.VnPay;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionStatusDTO implements Serializable {
    private int status;
    private String message;
    private String data;
}
