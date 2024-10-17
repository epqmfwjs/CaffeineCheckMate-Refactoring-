package com.project.ReCCM.Repository.custom;

public enum Shot {
    OneShot("1 shot"),
    TwoShot("2 shot"),
    ThreeShot("3 shot"),
    FourShot("4 shot");

    private final String displayName;

    Shot(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
