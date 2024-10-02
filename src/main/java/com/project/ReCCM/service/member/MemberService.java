package com.project.ReCCM.service.member;

import com.project.ReCCM.Repository.member.MemberInfoDto;
import com.project.ReCCM.Repository.member.MemberJoinDto;
import com.project.ReCCM.domain.member.Member;
import com.project.ReCCM.domain.member.MemberGender;
import com.project.ReCCM.domain.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;
import java.util.UUID;

@Service
public class MemberService {

    private static final String UPLOAD_DIR = "C:/upload/";

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void saveMember(MemberJoinDto memberJoinDto) throws IOException {
        Member member = new Member();

        // 생년월일이 비어있으면 기본값 설정
        if (memberJoinDto.getMemberAge() != null) {
            member.setMemberAge(memberJoinDto.getMemberAge());
        } else {
            member.setMemberAge(LocalDate.of(2000, 1, 1));  // 기본값으로 2000년 1월 1일을 설정
        }

        // DTO에서 멤버 필드들 설정
        member.setMemberId(memberJoinDto.getMemberId());
        member.setMemberName(memberJoinDto.getMemberName());
        member.setMemberEmail(memberJoinDto.getMemberEmail());
        member.setMemberAge(memberJoinDto.getMemberAge());
        member.setMemberWeight(memberJoinDto.getMemberWeight());
        member.setMemberPhone(memberJoinDto.getMemberPhone());
        member.setMemberGender(MemberGender.valueOf(memberJoinDto.getMemberGender()));

        MultipartFile imgReal = memberJoinDto.getImgReal(); // 단일 파일 처리

        // 디렉토리 생성
        File dir = new File(UPLOAD_DIR);
        if (!dir.exists()) {
            dir.mkdirs();  // 경로가 존재하지 않으면 생성
        }

        // 이미지 파일 처리
        if (imgReal != null && !imgReal.isEmpty()) {
            // 파일 이름을 안전하게 처리 (경로 제거)
            String originalFileName = StringUtils.cleanPath(imgReal.getOriginalFilename());

            // 고유한 파일 이름 생성 (예: UUID로 고유 이름 생성)
            String fileExtension = getFileExtension(originalFileName);
            String fileName = UUID.randomUUID().toString() + "." + fileExtension;

            // 파일 저장 경로 설정
            Path filePath = Paths.get(UPLOAD_DIR + fileName);

            // 이미지 파일 저장
            Files.copy(imgReal.getInputStream(), filePath);

            // 이미지 파일 이름을 엔티티에 설정 (파일명 저장)
            member.setImgReal(fileName);
        }

        // 비밀번호 암호화
        member.setPassword(passwordEncoder.encode(memberJoinDto.getPassword()));

        // 멤버 정보 저장
        memberRepository.save(member);
    }

    // 파일 확장자 추출 유틸리티 메소드
    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return ""; // 확장자가 없는 경우
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    // 멤버 정보 요청시 dto 맵핑
    public MemberInfoDto getMemberInfo(Long memberId) {
        Optional<Member> member = memberRepository.findById(memberId);
        LocalDate memberAge =  member.get().getMemberAge();

        int age = calculateAge(memberAge) + 1;  //  나이계산하기 메소드호출 Period를 사용해서 현재날짜와의 차이로 계산

        String gender = member.get().getMemberGender().name();
        if(gender.equals("Male")){
            gender = "남성";
        }else{
            gender = "여성";
        }



        MemberInfoDto memberInfoDto = new MemberInfoDto(

                member.get().getMemberId(),
                member.get().getMemberName(),
                member.get().getMemberEmail(),
                age,
                member.get().getMemberWeight(),
                member.get().getMemberPhone(),
                gender,
                member.get().getImgReal()

        );
        return memberInfoDto;

    }

    // 나이를 계산하는 메서드
    public int calculateAge(LocalDate memberAge) {
        if (memberAge == null) {
            throw new IllegalArgumentException("생년월일 제대로 안들어옴");
        }
        LocalDate currentDate = LocalDate.now();
        return Period.between(memberAge, currentDate).getYears();
    }
}
