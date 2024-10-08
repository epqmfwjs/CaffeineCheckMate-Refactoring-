package com.project.ReCCM.service.main;


import com.project.ReCCM.Repository.main.CalculatorResponseDto;
import com.project.ReCCM.domain.main.Calculator;
import com.project.ReCCM.domain.main.CalculatorRepository;
import com.project.ReCCM.domain.member.Member;
import com.project.ReCCM.domain.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;


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


    // 카페인 계산기 중첩저장 및 로드
    public CalculatorResponseDto calculator(int caffeine, Long memberId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 멤버가 존재하지 않습니다."));

        LocalDate today = LocalDate.now(); // 현재 날짜 가져오기
        Optional<Calculator> optionalCalculator = calculatorRepository.findByMemberIdAndCreatedDate(memberId, today);

        int finalCaffeine;

        if (optionalCalculator.isPresent()) {
            // 값이 존재할 경우, 기존 카페인 양과 더함
            int sumCaffeine = optionalCalculator.get().getCaffeine() + caffeine;
            finalCaffeine = sumCaffeine; // sumCaffeine 값을 finalCaffeine에 저장

            // 기존 엔티티를 업데이트
            Calculator calculatorToUpdate = optionalCalculator.get();
            calculatorToUpdate.setCaffeine(finalCaffeine);
            calculatorRepository.save(calculatorToUpdate); // save를 사용하여 업데이트
        } else {
            // 값이 존재하지 않을 경우, 새로운 카페인 양 저장
            finalCaffeine = caffeine; // 그냥 caffeine 값을 finalCaffeine에 저장
            Calculator newCalculator = new Calculator(finalCaffeine, member);
            calculatorRepository.save(newCalculator); // 새로운 엔티티 저장
        }


        int maxCaffeine;
        int age = calculateAge(member.getMemberAge()) + 1;

        // 성인, 청소년, 임신여부 에 따른 카페인 적정량 값 지정
        if(age>20){ // 성인 카페인 적정량 지정
            maxCaffeine = 400;
        }else if(age<20){ // 청소년 카페인 적정량 지정
            maxCaffeine = (int) (member.getMemberWeight()*2.5);
        }else{// 임신조건 나중에 설정해야함
            maxCaffeine = 300;
        }

        // 퍼센트 계산
        double resultCaffeine = Math.round(((double) finalCaffeine / maxCaffeine) * 100 * 100) / 100.0;

        CalculatorResponseDto calculatorResponseDto = new CalculatorResponseDto(
                member.getMemberId(), // memberId
                maxCaffeine,          // maxCaffeine
                finalCaffeine,             // 섭취한 카페인
                age,                  // 나이
                resultCaffeine        // 계산된 퍼센트 값
        );
        return calculatorResponseDto;
    }

    // 로그인 후 그래프 데이터 가져오기
    public CalculatorResponseDto getCaffeineData(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 멤버가 존재하지 않습니다."));

        LocalDate today = LocalDate.now(); // 현재 날짜 가져오기
        Optional<Calculator> optionalCalculator = calculatorRepository.findByMemberIdAndCreatedDate(memberId, today);

        int finalCaffeine;

        if (optionalCalculator.isPresent()) {
            finalCaffeine = optionalCalculator.get().getCaffeine(); // 존재하는 경우 카페인 값
        } else {
            finalCaffeine = 0; // 존재하지 않으면 카페인 섭취량을 0으로 설정
        }

        int maxCaffeine;
        int age = calculateAge(member.getMemberAge()) + 1;

        // 성인, 청소년, 임신여부 에 따른 카페인 적정량 값 지정
        if(age>20){ // 성인 카페인 적정량 지정
            maxCaffeine = 400;
        }else if(age<20){ // 청소년 카페인 적정량 지정
            maxCaffeine = (int) (member.getMemberWeight()*2.5);
        }else{// 임신조건 나중에 설정해야함
            maxCaffeine = 300;
        }

        // 퍼센트 계산
        double resultCaffeine = Math.round(((double) finalCaffeine / maxCaffeine) * 100 * 100) / 100.0;

        CalculatorResponseDto calculatorResponseDto = new CalculatorResponseDto(
                member.getMemberId(), // memberId
                maxCaffeine,          // maxCaffeine
                finalCaffeine,             // 섭취한 카페인
                age,                  // 나이
                resultCaffeine        // 계산된 퍼센트 값
        );
        return calculatorResponseDto;
    }

    public CalculatorResponseDto undoCaffeine(int caffeine, Long memberId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 멤버가 존재하지 않습니다."));

        LocalDate today = LocalDate.now(); // 현재 날짜 가져오기
        Optional<Calculator> optionalCalculator = calculatorRepository.findByMemberIdAndCreatedDate(memberId, today);

        int finalCaffeine;

        if (optionalCalculator.isPresent()) {
            // 값이 존재할 경우, 기존 카페인 양과 더함
            int sumCaffeine = optionalCalculator.get().getCaffeine() - caffeine;
            finalCaffeine = sumCaffeine; // sumCaffeine 값을 finalCaffeine에 저장

            // 기존 엔티티를 업데이트
            Calculator calculatorToUpdate = optionalCalculator.get();
            calculatorToUpdate.setCaffeine(finalCaffeine);
            calculatorRepository.save(calculatorToUpdate); // save를 사용하여 업데이트
        } else {
            // 값이 없으면 0으로 초기화
            finalCaffeine = 0;
        }


        int maxCaffeine;
        int age = calculateAge(member.getMemberAge()) + 1;

        // 성인, 청소년, 임신여부 에 따른 카페인 적정량 값 지정
        if(age>20){ // 성인 카페인 적정량 지정
            maxCaffeine = 400;
        }else if(age<20){ // 청소년 카페인 적정량 지정
            maxCaffeine = (int) (member.getMemberWeight()*2.5);
        }else{// 임신조건 나중에 설정해야함
            maxCaffeine = 300;
        }

        // 퍼센트 계산
        double resultCaffeine = Math.round(((double) finalCaffeine / maxCaffeine) * 100 * 100) / 100.0;

        CalculatorResponseDto calculatorResponseDto = new CalculatorResponseDto(
                member.getMemberId(), // memberId
                maxCaffeine,          // maxCaffeine
                finalCaffeine,             // 섭취한 카페인
                age,                  // 나이
                resultCaffeine        // 계산된 퍼센트 값
        );
        return calculatorResponseDto;
    }


    // 나이를 계산 모듈
    public int calculateAge(LocalDate memberAge) {
        if (memberAge == null) {
            throw new IllegalArgumentException("생년월일 제대로 안들어옴");
        }
        LocalDate currentDate = LocalDate.now();
        return Period.between(memberAge, currentDate).getYears();
    }

    public CalculatorResponseDto resetChart(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 멤버가 존재하지 않습니다."));
        
        LocalDate today = LocalDate.now(); // 현재 날짜 가져오기
        
        Optional<Calculator> optionalCalculator = calculatorRepository.findByMemberIdAndCreatedDate(memberId,today);

        optionalCalculator.ifPresent(calculator -> {
            // 엔티티 삭제
            calculatorRepository.delete(calculator);
            System.out.println(" memberPK값 : " + memberId + " 번의 " + today+ " 날짜 섭취카페인 양이 리셋되었음 " );
        });

        // 엔티티가 존재하지 않는 경우 처리
        if (optionalCalculator.isEmpty()) {
            System.out.println("memberPK값 : " + memberId + " 번의 " + today+ " 날짜로 섭취한 카페인이 없음");
        }

        int maxCaffeine;
        int age = calculateAge(member.getMemberAge()) + 1;

        // 성인, 청소년, 임신여부 에 따른 카페인 적정량 값 지정
        if(age>20){ // 성인 카페인 적정량 지정
            maxCaffeine = 400;
        }else if(age<20){ // 청소년 카페인 적정량 지정
            maxCaffeine = (int) (member.getMemberWeight()*2.5);
        }else{// 임신조건 나중에 설정해야함
            maxCaffeine = 300;
        }

        CalculatorResponseDto calculatorResponseDto =  new CalculatorResponseDto();
        calculatorResponseDto.setMaxCaffeine(maxCaffeine);

        return calculatorResponseDto;
    }
}
