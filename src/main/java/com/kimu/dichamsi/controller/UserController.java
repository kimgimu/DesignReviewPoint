package com.kimu.dichamsi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

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
    public String loginCheck(){
        return "redirect:/";
    }

    //회원가입페이지
    @GetMapping("/join")
    public String joinPage(){
        return "joinPage";
    }

    //회원가입메서드
    @PostMapping("/join")
    public String joinCheck(){
        return "redirect:/login";
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
