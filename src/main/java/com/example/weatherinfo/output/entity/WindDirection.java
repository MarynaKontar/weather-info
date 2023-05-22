package com.example.weatherinfo.output.entity;

public enum WindDirection {
    NORTH("NORTH"),
    NORTHEAST("NORTHEAST"),
    EAST("EAST"),
    SOUTHEAST("SOUTHEAST"),
    SOUTH("SOUTH"),
    SOUTHWEST("SOUTHWEST"),
    WEST("WEST"),
    NORTHWEST("NORTHWEST");

    private String direction;

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    WindDirection(String direction) {
        this.direction = direction;
    }
}
