package com.semester.tinder.dto.firebase;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetMessagedto {
    private int matches_id;
    private int sender_id;
    private int receive_id;
    private int limit = 20;
    private int offset = 0;
}
