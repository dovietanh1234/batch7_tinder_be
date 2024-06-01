package com.semester.tinder.dto.request.Follower;

public enum StatusFollower {
    MATCHING("Matching"),
    CANCELED("Canceled"),
    BLOCK("Block"),
    LIKE("Like");

    private String name;

    StatusFollower(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
