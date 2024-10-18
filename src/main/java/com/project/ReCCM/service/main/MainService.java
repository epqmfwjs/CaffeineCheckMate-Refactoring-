package com.project.ReCCM.service.main;

import com.project.ReCCM.Repository.main.CalculatorResponseDto;
import com.project.ReCCM.Repository.mypage.MyPageCaffeineResponseDto;
import com.project.ReCCM.domain.main.Calculator;
import com.project.ReCCM.domain.main.CalculatorRepository;
import com.project.ReCCM.domain.member.Member;
import com.project.ReCCM.domain.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MainService {

    @Autowired
    private final MemberRepository memberRepository;

    @Autowired
    private final CalculatorRepository calculatorRepository;

    public MainService(MemberRepository memberRepository, CalculatorRepository calculatorRepository) {
        this.memberRepository = memberRepository;
        this.calculatorRepository = calculatorRepository;
    }

    // 사용자에게 입력된 카페인 양을 저장하거나 기존 값에 더하는 함수
    public CalculatorResponseDto calculator(int caffeine, int calorie, int sugar, Long memberId) {
        return updateCaffeine(caffeine, calorie, sugar, memberId, true); // 카페인 추가 로직
    }

    // 로그인 후 그래프 데이터 가져오기
    public CalculatorResponseDto getCaffeineData(Long memberId) {
        return loadCaffeineData(memberId); // 저장된 카페인 데이터 로드
    }

    // 계산 실행 취소
    public CalculatorResponseDto undoCaffeine(int caffeine, int calorie, int sugar, Long memberId) {
        return updateCaffeine(caffeine, calorie, sugar, memberId, false); // 카페인 감소 로직
    }

    // 계산기 차트 리셋
    public CalculatorResponseDto resetChart(Long memberId) {
        Member member = getMemberById(memberId); // 멤버 정보를 가져옴
        Optional<Calculator> optionalCalculator = getCalculatorForToday(memberId); // 오늘의 카페인 데이터 가져오기

        // 기존 카페인 기록이 있으면 삭제
        optionalCalculator.ifPresent(calculatorRepository::delete);

        int age = calculateAge(member.getMemberAge()) + 1; // 나이 계산
        int maxCaffeine = calculateMaxCaffeine(age, member.getMemberWeight()); // 나이와 체중에 따른 적정 카페인 계산

        // 결과 Dto 반환
        return new CalculatorResponseDto(member.getMemberId(), maxCaffeine, 0, 0, 0, age, 0.0);
    }

    // 카페인계산을 재사용하기위해 MyPageService -> MainService 호출 이래도 되는건가 싶지만 재활용이 가능하길래 해봄...
    public List<MyPageCaffeineResponseDto> getCafffeineCalendar(Long memberId, List<Calculator> calculators) {
        System.out.println("MyPageService -> MainService의 getCafffeineCalendar 메소드 들어옴");
        Member member = getMemberById(memberId); // 멤버 정보를 가져옴

        int age = calculateAge(member.getMemberAge()) + 1; // 나이 계산
        int maxCaffeine = calculateMaxCaffeine(age, member.getMemberWeight()); // 나이와 체중에 따른 적정 카페인 계산

        // 이벤트 데이터를 리스트로 구성
        return calculators.stream()
                .map(calculator -> {
                    MyPageCaffeineResponseDto event = new MyPageCaffeineResponseDto();
                    event.setTitle(calculatePercentage(calculator.getCaffeine(), maxCaffeine) + "%"); // 제목 설정
                    event.setStart(calculator.getCreatedDate());
                    event.setPercentage(calculatePercentage(calculator.getCaffeine(), maxCaffeine)); // 카페인 퍼센트 계산
                    return event;
                })
                .collect(Collectors.toList());

    }

    // ----------------------------- 반복되는 코드 모듈화 -------------------------------

    // 카페인 데이터 로드/업데이트 처리
    private CalculatorResponseDto updateCaffeine(int caffeine, int calorie, int sugar, Long memberId, boolean isAdd) {
        Member member = getMemberById(memberId); // 멤버 정보 가져오기
        Optional<Calculator> optionalCalculator = getCalculatorForToday(memberId); // 오늘의 카페인 정보 가져오기

        int finalCaffeine = optionalCalculator.map(calculator -> {
            // 카페인 정보가 있을 경우, 기존 카페인 양을 업데이트 (true/false 를 통해 삼항연산자로 더하거나 빼기)
            int updatedCaffeine = calculator.getCaffeine() + (isAdd ? caffeine : -caffeine);
            int updatedCalorie = calculator.getCalorie() + (isAdd ? calorie : -calorie);
            int updatedSugar = calculator.getSugar() + (isAdd ? sugar : -sugar);

            calculator.setCaffeine(updatedCaffeine);
            calculator.setCalorie(updatedCalorie);
            calculator.setSugar(updatedSugar);

            calculatorRepository.save(calculator);
            return updatedCaffeine;
        }).orElseGet(() -> {
            // 카페인 정보가 없으면 새로 생성 후 저장
            Calculator newCalculator = new Calculator(caffeine, calorie, sugar, member);
            calculatorRepository.save(newCalculator);
            return caffeine;
        });

        return buildCaffeineResponse(member, finalCaffeine, calorie, sugar); // 결과 Dto 생성 및 반환
    }

    // 카페인 데이터를 로드하는 메소드
    private CalculatorResponseDto loadCaffeineData(Long memberId) {
        Member member = getMemberById(memberId);
        // 오늘의 카페인 데이터를 가져오고, 람다식으로 없으면 0으로 초기화
        int finalCaffeine = getCalculatorForToday(memberId)
                .map(Calculator::getCaffeine) // 값이 있으면 getCaffeine() 호출
                .orElse(0); // 값이 없으면 0 반환
        int finalCalorie = getCalculatorForToday(memberId)
                .map(Calculator::getCalorie) // 값이 있으면 getCaffeine() 호출
                .orElse(0); // 값이 없으면 0 반환
        int finalSugar = getCalculatorForToday(memberId)
                .map(Calculator::getSugar) // 값이 있으면 getCaffeine() 호출
                .orElse(0); // 값이 없으면 0 반환
        return buildCaffeineResponse(member, finalCaffeine, finalCalorie, finalSugar);
    }

    // 멤버 ID로 멤버 객체 가져오기 (DB 조회)
    private Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 멤버가 존재하지 않습니다."));
    }

    // 오늘 날짜 기준으로 카페인 정보 조회 (Optional로 반환)
    private Optional<Calculator> getCalculatorForToday(Long memberId) {
        LocalDate today = LocalDate.now(); // 오늘 날짜 가져오기
        return calculatorRepository.findByMemberIdAndCreatedDate(memberId, today); // 오늘의 카페인 정보 가져오기
    }

    // 카페인 계산 결과 Dto 생성
    private CalculatorResponseDto buildCaffeineResponse(Member member, int finalCaffeine, int finalCalorie, int finalSugar) {
        int age = calculateAge(member.getMemberAge()) + 1; // 나이 계산
        int maxCaffeine = calculateMaxCaffeine(age, member.getMemberWeight()); // 적정 카페인 계산
        double resultCaffeine = calculatePercentage(finalCaffeine, maxCaffeine); // 카페인 퍼센트 계산

        // 결과 Dto 객체 생성 및 반환
        return new CalculatorResponseDto(
                member.getMemberId(),
                maxCaffeine,
                finalCaffeine,
                finalCalorie,
                finalSugar,
                age,
                resultCaffeine
        );
    }

    // 퍼센트 계산 모듈 (카페인 섭취량 대비 적정 카페인 양 퍼센트 계산)
    private double calculatePercentage(int finalCaffeine, int maxCaffeine) {
        return Math.round(((double) finalCaffeine / maxCaffeine) * 100 * 100) / 100.0; // 소수점 두 자리 반올림
    }

    // 나이 계산 모듈
    private int calculateAge(LocalDate memberAge) {
        if (memberAge == null) {
            throw new IllegalArgumentException("생년월일 제대로 안들어옴");
        }
        return Period.between(memberAge, LocalDate.now()).getYears(); // 생년월일을 기준으로 현재 나이 계산
    }

    // 적정 카페인 계산 모듈 (나이 및 체중을 기반으로 적정 카페인 양 계산)
    private int calculateMaxCaffeine(int age, double memberWeight) {
        if (age > 20) {
            return 400; // 성인일 경우 적정 카페인 양 400mg
        } else if (age < 20) {
            return (int) (memberWeight * 2.5); // 청소년일 경우 체중에 따른 카페인 양 계산
        } else {
            return 300; // 기타 조건 (임신 등)
        }
    }
}
