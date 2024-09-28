package com.project.ReCCM.controller.main;

import com.project.ReCCM.Repository.custom.CustomResponseDto;
import com.project.ReCCM.service.custom.CustomService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class mainPageRestController {

    private final CustomService customService;

    public mainPageRestController(CustomService customService) {
        this.customService = customService;
    }

    // 메인 화면 커스텀 게시물을 반환하는 엔드포인트
    @GetMapping("/mainCustom")
    public List<CustomResponseDto> getMainCustoms() {
        System.out.println("mainCustom 컨트롤러");
        // 좋아요 순으로 정렬된 커스텀 게시물 반환
        return customService.getMainCustomsByLikeCount();
    }
}
