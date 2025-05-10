package com.example.project.constant;

public enum Roles {
    USER, ADMIN;

    public String getRole() {
        return "ROLE_" + this.name();
    }
}
