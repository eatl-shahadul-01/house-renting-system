package bd.com.squarehealth.corelibrary.enumerations;

public enum UserRole {
    NONE("None"),
    REGULAR_USER("Regular User"),
    ADMIN("Admin");

    private final String value;

    UserRole(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
