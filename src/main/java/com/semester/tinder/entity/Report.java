package com.semester.tinder.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.query.sql.internal.ParameterRecognizerImpl;

import java.util.Date;

@Entity(name = "report")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String content;

    private Date report_time;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "u_report")
    private User user_report;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "u_accused")
    private User user_accused;

}
