package com.project.ReCCM.service.member;

import com.project.ReCCM.Repository.member.MemberInfoDto;
import com.project.ReCCM.Repository.member.MemberJoinDto;
import com.project.ReCCM.Repository.member.MemberUpdateDTO;
import com.project.ReCCM.domain.main.Calculator;
import com.project.ReCCM.domain.main.CalculatorRepository;
import com.project.ReCCM.domain.member.Member;
import com.project.ReCCM.domain.member.MemberGender;
import com.project.ReCCM.domain.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private static final String DEFAULT_IMAGE_PATH = "/img/default-image.png"; // 기본 이미지 경로

    @Autowired
    private final MemberRepository memberRepository;

    @Autowired
    private final CalculatorRepository calculatorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, CalculatorRepository calculatorRepository) {
        this.memberRepository = memberRepository;
        this.calculatorRepository = calculatorRepository;
    }


    // 회원가입 로직
    public void saveMember(MemberJoinDto memberJoinDto) throws IOException {
        Member member = new Member();

        // ID 중복 체크 (세이브하기전 한번더 검증)
        if (memberRepository.findByMemberId(memberJoinDto.getMemberId()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 ID입니다.");
        }
        // 비밀번호 확인 (세이브하기전 한번더 검증)
        if (!memberJoinDto.getPassword().equals(memberJoinDto.getConfirmPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 이미지 저장 후 경로 저장
        MultipartFile imgReal = memberJoinDto.getImgReal();
        String imagePath = DEFAULT_IMAGE_PATH; // 기본 이미지 경로

        if (imgReal != null && !imgReal.isEmpty()) {
            // 업로드된 이미지가 있을 경우 처리
            String originalFileName = StringUtils.cleanPath(imgReal.getOriginalFilename());
            String fileExtension = getFileExtension(originalFileName);
            String fileName = UUID.randomUUID() + "." + fileExtension;

            // 파일 저장 경로 설정
            Path filePath = Paths.get(UPLOAD_DIR + fileName);

            // 디렉토리 생성 (없을 경우)
            File dir = new File(UPLOAD_DIR);
            if (!dir.exists()) {
                dir.mkdirs();  // 경로가 존재하지 않으면 생성
            }

            // 이미지 파일 저장
            Files.copy(imgReal.getInputStream(), filePath);

            // 파일명 저장 (이미지가 있으면 업로드한 이미지 경로를 사용)
            imagePath = fileName;
        }

        // 이미지 경로 설정 (기본 이미지 또는 업로드된 이미지 경로)
        member.setImgReal(imagePath);

        // 생년월일이 비어있으면 기본값 설정
        if (memberJoinDto.getMemberAge() != null) {
            member.setMemberAge(memberJoinDto.getMemberAge());
        } else {
            member.setMemberAge(LocalDate.of(2000, 1, 1));  // 기본값으로 2000년 1월 1일을 설정
        }

        // DTO에서 멤버 필드들 설정
        member.setMemberId(memberJoinDto.getMemberId());
        member.setMemberName(memberJoinDto.getMemberName());
        member.setMemberEmail(memberJoinDto.getEmail());
        member.setMemberAge(memberJoinDto.getMemberAge());
        member.setMemberWeight(memberJoinDto.getMemberWeight());
        member.setMemberPhone(memberJoinDto.getMemberPhone());
        member.setMemberGender(MemberGender.valueOf(memberJoinDto.getMemberGender()));
        member.setAddress(memberJoinDto.getAddress());
        member.setDetailAddress(memberJoinDto.getDetailAddress());


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
        LocalDate today = LocalDate.now();
        Optional<Calculator> todayCaffeine = calculatorRepository.findByMemberIdAndCreatedDate(memberId, today);

        LocalDate memberAge = member.get().getMemberAge();

        int age = calculateAge(memberAge) + 1;  //  나이계산하기 메소드호출 Period를 사용해서 현재날짜와의 차이로 계산
        int maxCaffeine = calculateMaxCaffeine(age, member.get().getMemberWeight());
        int todayCaffeineNow = todayCaffeine.map(Calculator::getCaffeine).orElse(0);

        String gender = member.get().getMemberGender().name();

        if (gender.equals("Male")) {
            gender = "남성";
        } else {
            gender = "여성";
        }


        return new MemberInfoDto(
                member.get().getMemberId(),
                member.get().getMemberName(),
                member.get().getMemberEmail(),
                age,
                member.get().getMemberWeight(),
                member.get().getMemberPhone(),
                gender,
                member.get().getImgReal(),
                todayCaffeineNow,
                maxCaffeine
        );
    }

    private int calculateMaxCaffeine(int age, double memberWeight) {
        if (age > 20) {
            return 400; // 성인일 경우 적정 카페인 양 400mg
        } else if (age < 20) {
            return (int) (memberWeight * 2.5); // 청소년일 경우 체중에 따른 카페인 양 계산
        } else {
            return 300; // 기타 조건 (임신 등)
        }
    }

    // 나이를 계산하는 메서드
    public int calculateAge(LocalDate memberAge) {
        if (memberAge == null) {
            throw new IllegalArgumentException("생년월일 제대로 안들어옴");
        }
        LocalDate currentDate = LocalDate.now();
        return Period.between(memberAge, currentDate).getYears();
    }

    public boolean isMemberIdTaken(String memberId) {
        return memberRepository.findByMemberId(memberId).isPresent();
    }

    // 수정폼 : 멤버 정보 불러오기
    public MemberUpdateDTO getUpdateMemberInfo(String username) {
        Member member = memberRepository.findByMemberId(username)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        return MemberUpdateDTO.builder()
                .memberId(member.getMemberId())
                .memberName(member.getMemberName())
                .memberAge(member.getMemberAge())
                .memberWeight(member.getMemberWeight())
                .memberPhone(member.getMemberPhone())
                .email(member.getMemberEmail())
                .memberGender(member.getMemberGender().name())
                .address(member.getAddress())
                .detailAddress(member.getDetailAddress())
                .imgReal(member.getImgReal()) // 기존 이미지 URL을 설정
                .build();
    }

    // 멤버 정보 수정
    public void updateMemberInfo(String username, MemberUpdateDTO memberUpdateDTO) throws IOException {
        System.out.println("username : "+username);

        Member member = memberRepository.findByMemberId(username)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        // 비밀번호 경우 입력 시에만 일치 여부 확인
        if (memberUpdateDTO.getPassword() != null && !memberUpdateDTO.getPassword().isEmpty()) {
            if (!memberUpdateDTO.getPassword().equals(memberUpdateDTO.getConfirmPassword())) {
                throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
            }
            member = member.toBuilder()
                    .password(passwordEncoder.encode(memberUpdateDTO.getPassword()))
                    .build();
        }

        MultipartFile imgReal = memberUpdateDTO.getNewImgReal(); // 신규 이미지
        String imagePath = member.getImgReal(); // 기존 이미지 경로

        // 신규 이미지가 있는 경우 처리
        if (imgReal != null && !imgReal.isEmpty()) {
            // 기존 이미지 삭제 (기본 이미지가 아닌 경우만 삭제)
            if (imagePath != null && !imagePath.equals(DEFAULT_IMAGE_PATH)) {
                Path oldFilePath = Paths.get(UPLOAD_DIR + imagePath);
                try {
                    Files.deleteIfExists(oldFilePath);  // 기존 이미지 파일 삭제
                } catch (IOException e) {
                    // 삭제 실패 시 처리 (로그 출력 또는 예외 처리)
                    System.out.println("삭제실패");
                    e.printStackTrace();
                }
            }

            // 업로드된 이미지가 있을 경우 처리
            String originalFileName = StringUtils.cleanPath(imgReal.getOriginalFilename());
            String fileExtension = getFileExtension(originalFileName);
            String fileName = UUID.randomUUID() + "." + fileExtension;

            // 파일 저장 경로 설정
            Path filePath = Paths.get(UPLOAD_DIR + fileName);

            // 디렉토리 생성 (없을 경우)
            File dir = new File(UPLOAD_DIR);
            if (!dir.exists()) {
                dir.mkdirs();  // 경로가 존재하지 않으면 생성
            }

            // 이미지 파일 저장
            Files.copy(imgReal.getInputStream(), filePath);

            // 신규 이미지 파일 경로로 설정
            imagePath = fileName;
        }
        member = member.toBuilder()
                .memberName(memberUpdateDTO.getMemberName())
                .memberAge(memberUpdateDTO.getMemberAge())
                .memberPhone(memberUpdateDTO.getMemberPhone())
                .memberEmail(memberUpdateDTO.getEmail())
                .memberGender(MemberGender.valueOf(memberUpdateDTO.getMemberGender()))
                .memberWeight(memberUpdateDTO.getMemberWeight())
                .address(memberUpdateDTO.getAddress())
                .detailAddress(memberUpdateDTO.getDetailAddress())
                .imgReal(imagePath)
                .build();
        memberRepository.save(member);

        // 현재 세션의 사용자 정보를 업데이트
        CustomMemberDetails updatedUserDetails = new CustomMemberDetails(member);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(updatedUserDetails, null, updatedUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}
