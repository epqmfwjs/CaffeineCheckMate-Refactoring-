package com.project.ReCCM.controller.member;


import com.project.ReCCM.Repository.member.MemberJoinDto;
import com.project.ReCCM.domain.member.Member;
import com.project.ReCCM.service.member.MemberService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @GetMapping("/login")
    public String showLoginFrm() {
        System.out.println("로그인페이지 들어옴");
        return "login";
    }


    @GetMapping("/join")
    public String showRegistrationForm() {
        System.out.println("가입페이지 들어옴");
        return "join";
    }

    @PostMapping("/member/login")
    public String login(@RequestParam String memberId,
                        @RequestParam String password,
                        RedirectAttributes redirectAttributes) {
        // 여기서 사용자 인증 로직을 추가할 수 있습니다.
        // 예: 사용자가 입력한 username과 password를 확인

        // 예시로, 성공적으로 로그인했다면
        redirectAttributes.addFlashAttribute("message", "로그인 성공");
        return "redirect:/"; // 로그인 성공 시 리다이렉트할 URL
    }

    @PostMapping("/join")
    public String registerMember(@Valid @ModelAttribute("memberDto") MemberJoinDto memberJoinDto,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes) {
        // 서버에서 유효성 검사 후 에러가 있을 경우 다시 폼으로 리다이렉트
        if (bindingResult.hasErrors()) {
            // 에러 메시지와 함께 클라이언트로 다시 리다이렉트
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/member/join";
        }
        // 날짜 유효성 확인
        if (memberJoinDto.getMemberAge() == null || memberJoinDto.getMemberAge().isAfter(LocalDate.now())) {
            bindingResult.rejectValue("memberAge", "error.memberAge", "유효한 생년월일을 입력해주세요.");
            return "redirect:/member/join";
        }
        System.out.println("검증성공 저장하러 가기전 ");
        // 성공 처리 로직 (예: DB에 저장)

        return "redirect:/member/success";
    }

}
