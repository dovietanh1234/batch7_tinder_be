package com.semester.tinder.dto.firebase;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageDTO {

    private int user_id;

    private MultipartFile image;

    private MultipartFile image2;

    private MultipartFile image3;

    private MultipartFile image4;

    private MultipartFile image5;


}
