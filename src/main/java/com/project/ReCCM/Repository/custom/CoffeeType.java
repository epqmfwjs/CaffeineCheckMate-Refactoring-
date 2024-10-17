package com.project.ReCCM.Repository.custom;

public enum CoffeeType {
    디카페인("디카페인"),
    에스프레소("에스프레소"),
    TEA("차(Tea)"),
    라떼("라떼"),
    에이드("에이드");

    private final String displayName;

    CoffeeType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
