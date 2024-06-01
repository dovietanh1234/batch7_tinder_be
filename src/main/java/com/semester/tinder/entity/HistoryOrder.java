package com.semester.tinder.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity(name="history_order")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoryOrder {

    @Id
    private int id;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "u_id")
    private User user;

    private double vnp_amount;

    private String vnp_bank_code;
    private String vnp_card_type;
    private Date vnp_pay_date;
    private String vnp_tmn_code;
}
