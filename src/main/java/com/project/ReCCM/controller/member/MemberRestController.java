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
}