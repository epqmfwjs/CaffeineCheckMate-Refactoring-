package com.project.ReCCM.controller.member;


import com.project.ReCCM.Repository.member.MemberJoinDto;
import com.project.ReCCM.Repository.member.MemberUpdateDTO;
import com.project.ReCCM.service.member.CustomMemberDetails;
import com.project.ReCCM.service.member.MemberService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
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
    public String showRegistrationForm(Model model) {
        System.out.println("가입페이지 들어옴");
        model.addAttribute("memberJoinDto", new MemberJoinDto());
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

    // 회원가입
    @PostMapping("/join")
    public String registerMember(@Valid @ModelAttribute("memberJoinDto") MemberJoinDto memberJoinDto,
                                 BindingResult bindingResult,
                                 Model model) throws IOException {

        System.out.println("joinDto 데이터 확인 : " + memberJoinDto.toString());

        // 유효성 검사 진행 후 에러 메세지 전달
        if (bindingResult.hasErrors()) {
            for (FieldError error : bindingResult.getFieldErrors()) {
                model.addAttribute(error.getField() + "Error", error.getDefaultMessage());
            }
            model.addAttribute("memberJoinDto", memberJoinDto);
            return "join";
        }
        // Password / ConfirmPassword 일치 여부 확인
        if (!memberJoinDto.getPassword().equals(memberJoinDto.getConfirmPassword())) {
            model.addAttribute("passwordError", "비밀번호가 일치하지 않습니다.");
            return "join";
        }

        memberService.saveMember(memberJoinDto);

        return "redirect:/member/joinSuccess";
    }

    // 회원 가입 성공 페이지
    @GetMapping("/joinSuccess")
    public String joinSuccess() {
        System.out.println("가입성공화면 띄우기");
        return "joinSuccess";
    }

    // 이메일 인증 팝업
    @GetMapping("/emailVerification")
    public String emailVerification() {
        return "emailVerification";
    }

    // 수정 폼 조회
    @GetMapping("/update")
    public String updateForm(@AuthenticationPrincipal CustomMemberDetails userDetails, Model model) {
        MemberUpdateDTO memberUpdateDTO = memberService.getUpdateMemberInfo(userDetails.getMemberId());

        model.addAttribute("memberUpdateDTO", memberUpdateDTO);
        return "update";
    }

    // 회원 정보 수정 처리
    @PostMapping("/update")
    public String update(@AuthenticationPrincipal CustomMemberDetails userDetails,
                         @Valid @ModelAttribute MemberUpdateDTO memberUpdateDTO,
                         BindingResult bindingResult,
                         Model model) {
        
        System.out.println("업데이트 드러옴");
        System.out.println("memberUpdateDTO : " + memberUpdateDTO.toString());
        if (bindingResult.hasErrors()) {
            for (FieldError error : bindingResult.getFieldErrors()) {
                System.out.println("업데이트 컨트롤러단 에러 1");
                model.addAttribute(error.getField() + "Error", error.getDefaultMessage());
            }
            return "update";
        }
        try {
            memberService.updateMemberInfo(userDetails.getMemberId(), memberUpdateDTO);
        } catch (IllegalArgumentException e) {
            System.out.println("memberUpdateDTO : " + memberUpdateDTO.toString());
            System.out.println("업데이트 컨트롤러단 에러 2");
            model.addAttribute("error", e.getMessage());
            return "update";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "redirect:/"; // 수정 성공 시 loginOk 페이지로 리다이렉트
    }
}
