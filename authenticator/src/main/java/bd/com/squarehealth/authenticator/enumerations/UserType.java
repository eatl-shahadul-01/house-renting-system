package bd.com.squarehealth.authenticator.enumerations;

public enum UserType {
    NONE("None"),
    REGULAR_USER("Regular User"),
    ADMIN("Admin");

    private final String value;

    UserType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
