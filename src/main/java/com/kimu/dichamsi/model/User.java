package com.kimu.dichamsi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ID를 자동 생성하도록 설정합니다.

    private String userEmail;
    private String userPassword;
    private String userNickname;
    private String username;
    private String userPhoneNumber;
    private String userAddress;
    private LocalDate userBirthday;

    public UserDTO toDTO(){
        return UserDTO.builder()
                .userEmail(userEmail)
                .userPassword(userPassword)
                .userNickname(userNickname)
                .username(username)
                .userAddress(userAddress)
                .userBirthday(userBirthday)
                .build();
    }

}
