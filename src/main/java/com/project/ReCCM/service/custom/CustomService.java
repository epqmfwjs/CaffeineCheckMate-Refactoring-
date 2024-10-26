package com.project.ReCCM.service.custom;

import com.project.ReCCM.Repository.custom.CustomPostRequestDto;
import com.project.ReCCM.Repository.custom.CustomResponseDto;
import com.project.ReCCM.domain.custom.CountRepository;
import com.project.ReCCM.domain.custom.Custom;
import com.project.ReCCM.domain.custom.CustomRepository;
import com.project.ReCCM.domain.member.Member;
import com.project.ReCCM.domain.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomService {
    
    //윈도우 경로
    //private static final String UPLOAD_DIR = "C:/upload/";

    // 리눅스 경로
    private static final String UPLOAD_DIR = "/upload/";
    @Autowired
    private CustomRepository customRepository;
    @Autowired
    private CountRepository countRepository;
    @Autowired
    private MemberRepository memberRepository;

    // 키워드를 이용한 커피 검색
    public List<Custom> searchCoffee(String keyword) {
        return customRepository.searchCoffee(keyword);
    }

    public void createCustomPost(CustomPostRequestDto requestDto) throws IOException {
        Custom custom = new Custom(requestDto);

        MultipartFile[] imgReal = requestDto.getImgReal();

        Optional<Member> optionalMember = memberRepository.findById(requestDto.getMemberPK());
        Member member = optionalMember.get();
        custom.setMember(member);

        File dir = new File(UPLOAD_DIR);
        if (!dir.exists()) {
            dir.mkdirs();  // 경로가 존재하지 않으면 생성
        }

        // 이미지 파일 처리
        if (imgReal != null && imgReal.length > 0) {
            MultipartFile image = imgReal[0]; // 예를 들어 첫 번째 이미지만 처리
            if (!image.isEmpty()) {
                String fileName = StringUtils.cleanPath(image.getOriginalFilename());
                File file = new File(UPLOAD_DIR + fileName);
                file.getParentFile().mkdirs(); // 부모 디렉토리 생성
                image.transferTo(file);

                // 이미지 파일 이름을 엔티티에 설정
                custom.setImgReal(fileName);
            }
        }
        customRepository.save(custom);
    }

    // 커스틈리스트 가져오기
    public List<Custom> getAllCustomPosts() {
        return customRepository.findAllByOrderByCreatedDateDesc();
    }

    // 페이징처리
//    public Page<Custom> getAllCustomPosts(Pageable pageable) {
//        return customRepository.findAll(pageable); // JpaRepository의 기본 페이지네이션 메소드 사용
//    }


    // 메인페이지 좋아요순 커스텀게시물 조회 반환
    public List<CustomResponseDto> getMainCustomsByLikeCount() {

        List<Custom> customs = customRepository.findAll();
        System.out.println(customs);

        // 좋아요 수에 따라 내림차순으로 정렬
        return customs.stream()
                .sorted((c1, c2) -> Integer.compare(c2.getLikesCount(), c1.getLikesCount()))
                .map(custom -> new CustomResponseDto(custom))  // 엔티티를 DTO로 변환
                .collect(Collectors.toList());
    }

    //게시물 좋아요 갯수 가져오기
    public int getLikes(Long postId) {
        int currentLikesCount = customRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시물이 존재하지 않습니다."))
                .getLikesCount();
        return currentLikesCount;
    }
}
