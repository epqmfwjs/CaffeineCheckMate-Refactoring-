package com.project.ReCCM.controller.member;


import com.project.ReCCM.domain.member.Member;
import com.project.ReCCM.service.member.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String registerUser(@RequestParam String memberId,@RequestParam String memberName, @RequestParam String password) {
        System.out.println("가입요청 들어옴");
        System.out.println("memberId" + " : " + memberId + "memberName" + " : " + memberName + "password" + " : " + password);
        Member member = new Member();
        member.setMemberId(memberId);
        member.setMemberName(memberName);
        member.setPassword(password);  // 비밀번호는 MemberService에서 암호화
        memberService.saveMember(member);
        return "redirect:/";
    }
}
