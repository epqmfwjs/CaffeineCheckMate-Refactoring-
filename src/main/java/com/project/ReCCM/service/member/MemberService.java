package com.project.ReCCM.service.member;

import com.project.ReCCM.Repository.member.MemberJoinDto;
import com.project.ReCCM.domain.member.Member;
import com.project.ReCCM.domain.member.MemberRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

import static com.project.ReCCM.controller.member.MemberController.UPLOAD_DIR_MEMBER;


@Service
public class MemberService {

    private static final String UPLOAD_DIR_MEMBER = "upload-dir-member/";
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Member saveMember(MemberJoinDto memberJoinDto) {

        MultipartFile imgReal = memberJoinDto.getImgReal();

        File dir = new File(UPLOAD_DIR_MEMBER);
        if (!dir.exists()) {
            dir.mkdirs();  // 경로가 존재하지 않으면 생성
        }

        // 이미지 파일 처리
        if (imgReal != null && imgReal.length > 0) {
            MultipartFile image = imgReal[0]; // 예를 들어 첫 번째 이미지만 처리
            if (!image.isEmpty()) {
                String fileName = StringUtils.cleanPath(image.getOriginalFilename());
                File file = new File(UPLOAD_DIR_MEMBER + fileName);
                file.getParentFile().mkdirs(); // 부모 디렉토리 생성
                image.transferTo(file);

                // 이미지 파일 이름을 엔티티에 설정
                //custom.setImgReal(fileName);
            }
        }


        // 비밀번호 암호화
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        return memberRepository.save(member);
    }
}
