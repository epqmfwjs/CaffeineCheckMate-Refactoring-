package com.project.ReCCM.controller;

import com.project.ReCCM.domain.member.Member;
import com.project.ReCCM.domain.member.MemberRepository;
import com.project.ReCCM.service.member.CustomMemberDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
public class FrontController {

    private final MemberRepository memberRepository;

    public FrontController(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @GetMapping("/")
    public String home(Authentication authentication, Model model){
        String memberName = null; //  이름
        String memberId = null; // 아이디
        Optional<Member> member; // 멤버 객체로받기
        Long memberPK = null; //pk값 받아놓기

        if (authentication != null && authentication.isAuthenticated()) {
            CustomMemberDetails userDetails = (CustomMemberDetails) authentication.getPrincipal();
            memberName = userDetails.getUsername(); // Member의 이름을 가져옵니다.
            memberId = userDetails.getMemberId(); // Member의 이름을 가져옵니다.
            memberPK = userDetails.getId(); // Member의 PK값 을 가져옵니다.
            System.out.println(memberPK);
        }
        System.out.println("멤버이름 : " + memberName);
        System.out.println("멤버아이디 : " + memberId);
        System.out.println("멤버PK" + memberPK);
        member = memberRepository.findByMemberId(memberId);

        model.addAttribute("memberId", memberId);
        model.addAttribute("memberPK", memberPK);
        model.addAttribute("member", member);

        return "main";
    }

    @GetMapping("/product")
    public String coffeeList(Authentication authentication, Model model){
        String memberName = null; //  이름
        String memberId = null; // 아이디
        Optional<Member> member; // 멤버 객체로받기
        Long memberPK = null; //pk값 받아놓기

        if (authentication != null && authentication.isAuthenticated()) {
            CustomMemberDetails userDetails = (CustomMemberDetails) authentication.getPrincipal();
            memberName = userDetails.getUsername(); // Member의 이름을 가져옵니다.
            memberId = userDetails.getMemberId(); // Member의 이름을 가져옵니다.
            memberPK = userDetails.getId(); // Member의 PK값 을 가져옵니다.
            System.out.println(memberPK);
        }
        System.out.println("멤버이름 : " + memberName);
        System.out.println("멤버아이디 : " + memberId);
        System.out.println("멤버PK" + memberPK);
        member = memberRepository.findByMemberId(memberId);

        model.addAttribute("memberId", memberId);
        model.addAttribute("memberPK", memberPK);
        model.addAttribute("member", member);
        return "coffeeList";
    }

    @GetMapping("/custom")
    public String custom(Authentication authentication, Model model){
        String memberName = null; //  이름
        String memberId = null; // 아이디
        Optional<Member> member; // 멤버 객체로받기
        Long memberPK = null; //pk값 받아놓기

        if (authentication != null && authentication.isAuthenticated()) {
            CustomMemberDetails userDetails = (CustomMemberDetails) authentication.getPrincipal();
            memberName = userDetails.getUsername(); // Member의 이름을 가져옵니다.
            memberId = userDetails.getMemberId(); // Member의 이름을 가져옵니다.
            memberPK = userDetails.getId(); // Member의 PK값 을 가져옵니다.
            System.out.println(memberPK);
        }
        System.out.println("멤버이름 : " + memberName);
        System.out.println("멤버아이디 : " + memberId);
        System.out.println("멤버PK" + memberPK);
        member = memberRepository.findByMemberId(memberId);

        model.addAttribute("memberId", memberId);
        model.addAttribute("memberPK", memberPK);
        model.addAttribute("member", member);
        return "custom";
    }
}
