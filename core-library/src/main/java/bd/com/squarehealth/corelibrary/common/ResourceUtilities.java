package bd.com.squarehealth.corelibrary.common;

import java.io.InputStream;

public final class ResourceUtilities {

    /**
     * Reads resource as text data.
     * @param resourcePath Path to resource. Note: Resource path must not start with slashes.
     * @return Returns resource as text data.
     */
    public static String readResourceAsString(String resourcePath) {
        InputStream inputStream = ResourceUtilities.class.getResourceAsStream("/" + resourcePath);
        String resourceAsString = StreamUtilities.readFromInputStream(inputStream);

        return resourceAsString;
    }
}
