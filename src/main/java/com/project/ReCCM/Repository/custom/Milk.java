package com.project.ReCCM.Repository.custom;

public enum Milk {
    일반우유("일반우유"),
    저지방우유("저지방우유"),
    무지방우유("무지방우유"),
    두유("두유");

    private final String displayName;

    Milk(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
