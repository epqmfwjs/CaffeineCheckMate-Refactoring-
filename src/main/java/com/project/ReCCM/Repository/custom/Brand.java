package com.project.ReCCM.Repository.custom;

public enum Brand {
    스타벅스("스타벅스"),
    투썸("투썸"),
    컴포즈("컴포즈"),
    메카커피("메카커피"),
    빽다방("빽다방"),
    탐앤탐스("탐앤탐스"),
    이디야("이디야");

    private final String displayName;

    Brand(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
