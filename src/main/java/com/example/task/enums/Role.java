package com.example.task.enums;

public enum Role {
    PROJECT_MANAGER,
    PRODUCT_MANAGER,
    DEVELOPER,
    TESTER,
    DELIVERY_MANAGER,
    NONE;

    public static Role getRole(String roleName) {
        for (Role value : Role.values()) {
            if (value.name().equals(roleName)) {
                return value;
            }
        }
        return Role.NONE;
    }

}
