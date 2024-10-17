package com.project.ReCCM.Repository.custom;

public enum Syrup {
    시럽1번("1번(펌프)"),
    시럽2번("2번(펌프)"),
    시럽3번("3번(펌프)"),
    시럽4번("4번(펌프)"),
    시럽5번("5번(펌프)");

    private final String displayName;

    Syrup(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}


