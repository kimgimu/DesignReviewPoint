package com.kimu.dichamsi.service;

import com.kimu.dichamsi.execption.AppException;
import com.kimu.dichamsi.execption.ErrorCode;
import com.kimu.dichamsi.model.CustomUserDetails;
import com.kimu.dichamsi.model.Member;
import com.kimu.dichamsi.model.MemberDTO;
import com.kimu.dichamsi.repository.MemberReository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Service
@Slf4j
public class MemberService implements UserDetailsService {

    private final MemberReository memberReository;
    private final BCryptPasswordEncoder encoder;
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

    public boolean emailCheck(MemberDTO memberDto){
        return memberReository.findByUserEmail(memberDto.getUserEmail()).isPresent();
    }

    public boolean nicknameCheck(MemberDTO memberDto){
        return memberReository.findByNickname(memberDto.getNickname()).isPresent();
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Member> memberEntity = memberReository.findByUserEmail(username);

        if(memberEntity.isPresent()) {
            return new CustomUserDetails(memberEntity.get());
        }else {
            throw new UsernameNotFoundException("유저 정보를 찾을 수 없습니다.");
        }
    }

}



