package com.project.ReCCM.controller;

import com.project.ReCCM.domain.member.Member;
import com.project.ReCCM.service.member.CustomMemberDetails;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FrontController {
    @GetMapping("/")
    public String home(Authentication authentication, Model model){
        String memberId = null; // String으로 변경
        if (authentication != null && authentication.isAuthenticated()) {
            CustomMemberDetails userDetails = (CustomMemberDetails) authentication.getPrincipal();
            memberId = userDetails.getUsername(); // Member의 이름을 가져옵니다.
        }
        model.addAttribute("memberId", memberId); // addText -> addAttribute로 수정

        return "main";
    }

    @GetMapping("/product")
    public String coffeeList(){
        return "coffeeList";
    }

    @GetMapping("/custom")
    public String custom(){
        return "custom";
    }
}
