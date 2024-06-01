package com.semester.tinder.dto.firebase;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteImageDTO {
    private int idUser;
    private String url;

}
