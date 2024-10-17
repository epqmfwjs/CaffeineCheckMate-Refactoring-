package com.project.ReCCM.Repository.custom;

public enum Whipped {
    휘핑크림("휘핑크림 추가"),
    드리즐("드리즐 추가"),
    자바칩("자바칩 추가");

    private final String displayName;

    Whipped(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
