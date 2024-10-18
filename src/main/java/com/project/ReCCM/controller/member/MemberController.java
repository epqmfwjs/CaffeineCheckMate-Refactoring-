package com.project.ReCCM.controller.member;


import com.project.ReCCM.Repository.member.MemberJoinDto;
import com.project.ReCCM.service.member.MemberService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

@Controller
@RequestMapping("/member")
public class MemberController {

    private static final String UPLOAD_DIR = "upload-dir/";
    @Autowired
    private MemberService memberService;

    @GetMapping("/login")
    public String showLoginFrm() {
        System.out.println("로그인페이지 들어옴");
        return "login";
    }

    public void MemberService() {
        // 디렉토리가 없으면 생성
        File dir = new File(UPLOAD_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
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

        //  성공적으로 로그인했다면
        redirectAttributes.addFlashAttribute("message", "로그인 성공");
        return "redirect:/"; // 로그인 성공 시 리다이렉트할 URL
    }

    @PostMapping("/join")
    public String registerMember(@Valid @ModelAttribute("memberDto") MemberJoinDto memberJoinDto,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes) throws IOException {

        System.out.println("join 컨트롤러들어옴");

        // 서버에서 유효성 검사 후 에러가 있을 경우 다시 폼으로 리다이렉트
        if (bindingResult.hasErrors()) {
            System.out.println("join : bindingResult.hasErrors()");
            // 에러 메시지와 함께 클라이언트로 다시 리다이렉트
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/member/join";
        }
        // 날짜 유효성 확인
        if (memberJoinDto.getMemberAge() == null || memberJoinDto.getMemberAge().isAfter(LocalDate.now())) {
            System.out.println("join : 날짜 유효성 확인");
            bindingResult.rejectValue("memberAge", "error.memberAge", "유효한 생년월일을 입력해주세요.");
            return "redirect:/member/join";
        }
        System.out.println("검증성공 저장하러 가기전 ");
        memberService.saveMember(memberJoinDto);
        // 성공 처리 로직

        return "redirect:/member/success";
    }

    // 이메일 인증 팝업
    @GetMapping("/emailVerification")
    public String emailVerification() {
        return "emailVerification";
    }

}
