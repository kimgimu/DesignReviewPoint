package com.kimu.dichamsi.controller;

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

    // 로그인 성공 후에 메인 페이지로 리디렉션
    @RequestMapping(value = "/loginSuccess", method = RequestMethod.GET)
    public String loginSuccess(HttpSession session) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        System.out.println(username);
        session.setAttribute("userEmail", username);
        return "redirect:/mainPage";
    }

    @RequestMapping(value = "/loginFail", method = RequestMethod.GET)
    public String loginFail(Model model) {
        model.addAttribute("failMessage","아이디 또는 비밀번호를 잘못 입력했습니다.");
        return "loginPage";
    }
}
