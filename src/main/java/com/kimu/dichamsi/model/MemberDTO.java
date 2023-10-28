package com.kimu.dichamsi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MemberDTO {

    //아이디
    private String userEmail;
    //비밀번호
    private String password;
    //이름
    private String username;
    //닉네임
    private String nickname;
    //전화번호
    private String telephone;

    public Member toEntity(BCryptPasswordEncoder encoder){
        return Member.builder()
                .userEmail(userEmail)
                .password(encoder.encode(password))
                .username(username)
                .nickname(nickname)
                .telephone(telephone)
                .build();
    }
}
