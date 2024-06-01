package com.semester.tinder.dto.firebase;

public enum StatusEnum {

    //DEFINE ENUMS:
    ERROR("ERROR"),
    SENT("SENT"),
    SEEN("SEEN");

    private String Name;

    private StatusEnum(String name) {
        Name = name;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
