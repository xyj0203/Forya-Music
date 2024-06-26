package com.example.music.entity.pojo.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GroupUser {
    private Long uId;
    private Long userId;
    private Long groupId;
    private String groupNickame;
}
