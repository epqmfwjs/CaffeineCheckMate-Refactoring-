package com.project.ReCCM.controller.main;

import com.project.ReCCM.Repository.custom.CustomResponseDto;
import com.project.ReCCM.Repository.main.CalculatorResponseDto;
import com.project.ReCCM.service.custom.CustomService;
import com.project.ReCCM.service.main.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class mainPageRestController {

    private final CustomService customService;

    private final MainService mainService;

    @Autowired
    public mainPageRestController(CustomService customService, MainService mainService) {
        this.customService = customService;
        this.mainService = mainService;
    }

    // 메인 화면 커스텀 게시물을 반환하는 엔드포인트
    @GetMapping("/mainCustom")
    public List<CustomResponseDto> getMainCustoms() {
        System.out.println("mainCustom 컨트롤러");
        // 좋아요 순으로 정렬된 커스텀 게시물 반환
        return customService.getMainCustomsByLikeCount();
    }
    
    // 임시 카페인계산 엔드포인트
    @GetMapping("/calculator")
    public CalculatorResponseDto calculator(){
        int caffeine = 75;
        Long memberId = 8L;

        CalculatorResponseDto resultCaffeineDto = mainService.calculator(caffeine,memberId);
        System.out.println("카페인 계산결과값 : "+resultCaffeineDto.getResultCaffeine()+"%");
        return resultCaffeineDto;
    }
}
