package com.kimu.dichamsi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserDTO {

    //아이디
    private String userEmail;
    //비밀번호
    private String userPassword;
    //닉네임
    private String userNickname;
    //이름
    private String username;
    //전화번호
    private String userPhoneNumber;
    //주소
    private String userAddress;
    //생년월일
    private LocalDate userBirthday;

    public User toEntity(){
        return User.builder()
                .userEmail(userEmail)
                .userPassword(userPassword)
                .userNickname(userNickname)
                .username(username)
                .userAddress(userAddress)
                .userBirthday(userBirthday)
                .build();
    }
}
