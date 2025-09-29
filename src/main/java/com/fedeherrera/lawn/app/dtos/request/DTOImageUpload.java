package com.fedeherrera.lawn.app.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DTOImageUpload {
    private Long userId;
    private String imageBase64;
}
