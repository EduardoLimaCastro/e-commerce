package com.eduardocastro.user_service.domain.enums;

public enum UserRole {
    ADMIN("Administrator"),
    USER("User"),
    COORDINATOR("Coordinator"),
    SUPERVISOR("Supervisor");

    private final String label;

    UserRole(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
