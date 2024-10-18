package com.project.ReCCM.controller.main;

import com.project.ReCCM.Repository.custom.CustomResponseDto;
import com.project.ReCCM.Repository.main.CaffeineRequest;
import com.project.ReCCM.Repository.main.CalculatorResponseDto;
import com.project.ReCCM.service.custom.CustomService;
import com.project.ReCCM.service.main.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        // 좋아요 순으로 정렬된 커스텀 게시물 반환
        return customService.getMainCustomsByLikeCount();
    }

    // 카페인계산 엔드포인트
    @PostMapping("/calculator")
    public ResponseEntity<CalculatorResponseDto> calculateCaffeine(@RequestBody CaffeineRequest request) {
        int caffeine = request.getCaffeineAmount();
        int calorie = request.getCalorie();
        int sugar = request.getSugar();

        System.out.println("지금확인중인 데이터 : " + " 칼로리 : " + calorie + "당분 : " + sugar);

        Long coffeeId = request.getCoffeeId();
        Long memberId = request.getMemberId();

        CalculatorResponseDto resultCaffeineDto = mainService.calculator(caffeine, calorie, sugar, memberId);

        return ResponseEntity.ok(resultCaffeineDto);
    }

    // 로그인 후 초기 그래프 데이터 가져오기
    @GetMapping("getCaffeineData")
    public CalculatorResponseDto getCaffeineData(@RequestParam Long memberId) {

        CalculatorResponseDto calculatorResponseDto = mainService.getCaffeineData(memberId);

        return calculatorResponseDto;
    }

    // 카페인 계산 실행취소
    @PostMapping("/undoCaffeine")
    public ResponseEntity<CalculatorResponseDto> undoCaffeine(@RequestBody CaffeineRequest request) {

        int caffeine = request.getCaffeineAmount();
        int calorie = request.getCalorie();
        int sugar = request.getSugar();

        Long coffeeId = request.getCoffeeId();
        Long memberId = request.getMemberId();

        CalculatorResponseDto resultCaffeineDto = mainService.undoCaffeine(caffeine, calorie, sugar, memberId);

        return ResponseEntity.ok(resultCaffeineDto);
    }

    // 리셋버튼 엔드포인트
    @PostMapping("/resetChart")
    public ResponseEntity<CalculatorResponseDto> resetChart(@RequestBody CaffeineRequest request) {
        Long memberId = request.getMemberId();

        CalculatorResponseDto resultCaffeineDto = mainService.resetChart(memberId);

        return ResponseEntity.ok(resultCaffeineDto);
    }
}
