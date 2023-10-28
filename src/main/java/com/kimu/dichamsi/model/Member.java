package com.kimu.dichamsi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ID를 자동 생성하도록 설정합니다.

    private String userEmail;
    private String password;
    private String username;
    private String nickname;
    private String telephone;

    public MemberDTO toDTO(){
        return MemberDTO.builder()
                .userEmail(userEmail)
                .password(password)
                .username(username)
                .nickname(nickname)
                .telephone(telephone)
                .build();
    }

}
