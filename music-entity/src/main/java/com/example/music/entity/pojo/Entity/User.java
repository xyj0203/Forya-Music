package com.example.music.entity.pojo.Entity;

import lombok.*;

import javax.validation.constraints.Email;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class User {
    private Long userId;
    private String account;
    @Email(message = "必须为邮箱格式")
    private String email;
    private String password;
    private Integer sex;
    private Date birthday;
    private String username = "未知";
    private String headImg = "https://profile-avatar.csdnimg.cn/f6f9d8c044624e33a9b58aba68376925_weixin_49919104.jpg!2";
    private Integer state = 0;
    private String area = "未知";
    private String signature ="未知";
}
