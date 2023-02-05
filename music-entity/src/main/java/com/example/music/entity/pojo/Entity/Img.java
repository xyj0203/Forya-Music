package com.example.music.entity.pojo.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Img {
    private String imgSrc;
    private Long id;
    private Long userId;
    private String imgDecription;
}
