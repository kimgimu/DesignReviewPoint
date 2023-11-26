package com.kimu.dichamsi.controller;

import com.kimu.dichamsi.model.CustomUserDetails;
import com.kimu.dichamsi.model.Member;
import com.kimu.dichamsi.model.MemberDTO;
import com.kimu.dichamsi.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RequestMapping("/user")
@Controller
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    //로그인페이지
    @GetMapping("/login")
    public String loginPage() {
        return "loginPage";
    }

    //회원가입페이지
    @GetMapping("/join")
    public String joinPage() {
        return "joinPage";
    }

    //회원가입메서드
    @PostMapping("/join")
    public String joinCheck(MemberDTO memberDTO) {
        memberService.join(memberDTO);
        return "redirect:/user/login";
    }

    @PostMapping("/email/check")
    @ResponseBody
    public ResponseEntity<?> emailCheck(MemberDTO memberDTO) {
        log.info("memberDTO = {}",memberDTO);
       boolean isEmail = memberService.emailCheck(memberDTO);
       if(!isEmail){
           return ResponseEntity.ok("해당 이메일로 가입이 가능합니다");
       } else {
           return ResponseEntity.badRequest().body("이미 가입 된 이메일입니다");
       }
    }

    @PostMapping("/nickname/check")
    @ResponseBody
    public ResponseEntity<?> nicknameCheck(MemberDTO memberDTO) {
        boolean isNickname = memberService.nicknameCheck(memberDTO);
        if(!isNickname){
            return ResponseEntity.ok("해당 닉네임으로 가입이 가능합니다");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이미 가입 된 닉네임입니다");
        }
    }



}
