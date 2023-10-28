package com.kimu.dichamsi.service;

import com.kimu.dichamsi.execption.AppException;
import com.kimu.dichamsi.execption.ErrorCode;
import com.kimu.dichamsi.model.Member;
import com.kimu.dichamsi.model.MemberDTO;
import com.kimu.dichamsi.repository.MemberReository;
import com.kimu.dichamsi.jwt.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberReository memberReository;
    private final BCryptPasswordEncoder encoder;
    @Value("${jwt.token.secret}")
    private String key;
    private Long expireTimeMs = 1000 * 60 * 60L;
    public MemberService(MemberReository memberReository, BCryptPasswordEncoder encoder) {
        this.memberReository = memberReository;
        this.encoder = encoder;
    }

    //회원가입
    public MemberDTO join(MemberDTO memberDTO){
        //이메일 중복
        memberReository.findByUserEmail(memberDTO.getUserEmail())
                .ifPresent(user -> {
                    throw new AppException(ErrorCode.USEREMAIL_DUPLICATED,"이미 가입된 이메일 입니다.");
                });
        //저장
        return memberReository.save(memberDTO.toEntity(encoder)).toDTO();
    }

    //로그인
    public String login(MemberDTO memberDTO){
        System.out.println("로그인 서비스단");
        String memberEmail = memberDTO.getUserEmail();
        //userEmail 없음
        Member selectdMember = memberReository.findByUserEmail(memberEmail)
                .orElseThrow(() -> new AppException(ErrorCode.USEREMAIL_NOT_FOUND,memberEmail+"이 없습니다."));
        //password 틀림 (매치 순서 중요! DTO , Entity)
        if(!encoder.matches(memberDTO.getPassword(),selectdMember.getPassword())){
            throw new AppException(ErrorCode.INVALID_PASSWORD,"패스워드를 잘못 입력 했습니다");
        }
        //성공시 토큰 발행
        String token = JwtTokenUtil.createToken(selectdMember.getUserEmail(),key,expireTimeMs);

        return token;
    }

}
