package com.project.ReCCM.Repository.main;

import com.project.ReCCM.domain.custom.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CalculatorResponseDto {

    private String memberId;
    private int maxCaffeine;
    private int drankCaffeine;
    private int age;
    private double resultCaffeine;


}
