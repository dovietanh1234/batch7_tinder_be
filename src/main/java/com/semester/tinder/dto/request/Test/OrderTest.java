package com.semester.tinder.dto.request.Test;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderTest implements Serializable {
    private int id_u;

    private double price;

    private int p_id;

    private String name;

}
