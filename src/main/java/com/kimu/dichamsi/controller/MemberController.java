package com.kimu.dichamsi.controller;

import com.kimu.dichamsi.model.MemberDTO;
import com.kimu.dichamsi.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/user")
@Controller
public class MemberController {

    private final MemberService memberService;
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    //메인페이지
    @GetMapping("/")
    public String mainPage(){
        return "mainPage";
    }

    //로그인페이지
    @GetMapping("/login")
    public String loginPage(){
        return "loginPage";
    }

    //로그인메서드
    @PostMapping("/login")
    public String loginCheck(MemberDTO memberDTO){
        String token = memberService.login(memberDTO);
        System.out.println(token);
        return "redirect:/";
    }

    //회원가입페이지
    @GetMapping("/join")
    public String joinPage(){
        return "joinPage";
    }

    //회원가입메서드
    @PostMapping("/join")
    public String joinCheck(MemberDTO memberDTO){
        memberService.join(memberDTO);
        return "redirect:/user/login";
    }

    //마이페이지
    @GetMapping("/mypage")
    public String mypage(){
        return "myPage";
    }

    //설정페이지
    @GetMapping("/setting")
    public String setting(){
        return "settingPage";
    }

}
