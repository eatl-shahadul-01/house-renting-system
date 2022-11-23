package bd.com.squarehealth.corelibrary.common.cryptography;

import bd.com.squarehealth.corelibrary.common.ResourceUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

public class HmacSha512PasswordEncoder implements PasswordEncoder {

    @Autowired
    private CryptographicService cryptographicService;

    private static final String HMAC_SHA512_SECRET_KEY_RESOURCE_NAME = "hmac-sha512-secret-key";
    private static final String HMAC_SHA512_SECRET_KEY = ResourceUtilities.readResourceAsString(HMAC_SHA512_SECRET_KEY_RESOURCE_NAME);

    @Override
    public String encode(CharSequence rawPassword) {
        try {
            return cryptographicService.calculateHmacSha512(rawPassword.toString(), HMAC_SHA512_SECRET_KEY);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        try {
            String newlyEncodedPassword = encode(rawPassword);

            return newlyEncodedPassword.equals(encodedPassword);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return false;
    }
}
