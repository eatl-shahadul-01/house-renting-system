package bd.com.squarehealth.corelibrary.enumerations;

public enum PostStatus {
    NONE("None"),
    PENDING("Pending"),
    APPROVED("Approved"),
    REJECTED("Rejected");

    private final String value;

    PostStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
