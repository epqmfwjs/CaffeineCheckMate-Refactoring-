package com.project.ReCCM.Repository.main;

import com.project.ReCCM.domain.custom.Comment;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class CalculatorResponseDto {

    private String memberId;
    private int maxCaffeine;
    private int drankCaffeine;
    private int drankCalorie;
    private int drankSugar;
    private int age;
    private double resultCaffeine;
    public CalculatorResponseDto(String memberId, int maxCaffeine, int drankCaffeine, int drankCalorie, int drankSugar, int age, double resultCaffeine) {
        this.memberId = memberId;
        this.maxCaffeine = maxCaffeine;
        this.drankCaffeine = drankCaffeine;
        this.drankCalorie = drankCalorie;
        this.drankSugar = drankSugar;
        this.age = age;
        this.resultCaffeine = resultCaffeine;
    }
}
