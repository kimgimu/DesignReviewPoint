package com.kimu.dichamsi.controller;

import com.kimu.dichamsi.model.CustomUserDetails;
import com.kimu.dichamsi.model.Member;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
public class MainController {

    @GetMapping("/mainPage")
    public String mainPage () {
        return "mainPage";
    }

    @GetMapping("/search")
    public String mainSearch(String request){
        System.out.println(request);
        return "redirect:/posting/search";
    }

    // 로그인 성공 후에 메인 페이지로 리디렉션
    @RequestMapping(value = "/loginSuccess", method = RequestMethod.GET)
    public String loginSuccess(HttpSession session) {
        String nickname = principalInfo().getNickname();
        session.setAttribute("nickname",nickname);
        return "redirect:/posting/main";
    }

    @RequestMapping(value = "/loginFail", method = RequestMethod.GET)
    public String loginFail(Model model) {
        model.addAttribute("failMessage","아이디 또는 비밀번호를 잘못 입력했습니다.");
        return "loginPage";
    }

    private Member principalInfo(){
        //시큐리티에서 닉네임 뽑아오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        return customUserDetails.getMember();
    }
}
