package com.semester.tinder.dto.request.Report;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.semester.tinder.entity.User;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportRequest {

    private String content;

    private Date report_time;

    private int user_report;

    private int user_accused;
}
