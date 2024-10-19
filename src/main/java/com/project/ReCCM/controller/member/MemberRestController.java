package com.project.ReCCM.controller.member;

import com.project.ReCCM.Repository.member.MemberInfoDto;
import com.project.ReCCM.service.member.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MemberRestController {

    @Autowired
    private final MemberService memberService;

    public MemberRestController(MemberService memberService) {

        this.memberService = memberService;
    }

    @GetMapping("/getMemberInfo")
    public ResponseEntity<MemberInfoDto> getMemberInfo(@RequestParam Long memberId) {
        MemberInfoDto memberInfoDto = memberService.getMemberInfo(memberId);
        return ResponseEntity.ok(memberInfoDto);
    }


    // ID 중복 체크
    @GetMapping("/member/check-id")
    public ResponseEntity<String> checkId(@RequestParam("memberId") String memberId) {
        if (memberId.length() < 6 || memberId.length() > 20) {
            return ResponseEntity.badRequest().body("아이디는 6자 이상, 20자 이하로 입력해주세요.");
        }

        if (memberService.isMemberIdTaken(memberId)) {
            return ResponseEntity.ok("이미 사용 중인 ID입니다.");
        }

        return ResponseEntity.ok("사용 가능한 ID입니다.");
    }
}