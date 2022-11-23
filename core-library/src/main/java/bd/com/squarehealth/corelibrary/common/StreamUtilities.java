package bd.com.squarehealth.corelibrary.common;

import java.io.InputStream;
import java.nio.charset.Charset;

public final class StreamUtilities {

    private static final int BUFFER_LENGTH = 8192;

    public static int readFromInputStream(byte[] buffer, int offset, int length, InputStream inputStream) {
        try {
            return inputStream.read(buffer, offset, length);
        } catch (Exception exception) {
            exception.printStackTrace();

            return -2;
        }
    }

    public static String readFromInputStream(InputStream inputStream) {
        byte[] buffer = new byte[BUFFER_LENGTH];
        int bytesRead = 0;
        StringBuilder stringBuilder = new StringBuilder(BUFFER_LENGTH);

        while ((bytesRead = StreamUtilities.readFromInputStream(buffer, 0, buffer.length, inputStream)) > -1) {
            stringBuilder.append(new String(buffer, 0, bytesRead, Charset.forName("UTF-8")));
        }

        return stringBuilder.toString();
    }
}
