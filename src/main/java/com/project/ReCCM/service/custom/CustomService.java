package com.project.ReCCM.service.custom;

import com.project.ReCCM.domain.custom.Custom;
import com.project.ReCCM.domain.custom.CustomRepository;
import com.project.ReCCM.domain.member.Member;
import com.project.ReCCM.domain.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class CustomService {
    @Autowired
    private CustomRepository customRepository;

    @Autowired
    private MemberRepository memberRepository;

    private static final String UPLOAD_DIR = "C:/upload/";

    // 키워드를 이용한 커피 검색
    public List<Custom> searchCoffee(String keyword) {
        return customRepository.searchCoffee(keyword);
    }

    public void createCustomPost(Long memberPK, String customTitle, String customContent, MultipartFile[] imgReal) throws IOException {
        Custom custom = new Custom(customTitle, customContent);

        Optional<Member> optionalMember = memberRepository.findById(memberPK);
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
    public List<Custom> getAllCustomPosts()
    {
        return customRepository.findAllByOrderByCreatedDateDesc();
    }
}
