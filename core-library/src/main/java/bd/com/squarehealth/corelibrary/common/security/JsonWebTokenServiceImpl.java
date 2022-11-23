package bd.com.squarehealth.corelibrary.common.security;

import bd.com.squarehealth.corelibrary.common.ResourceUtilities;
import bd.com.squarehealth.corelibrary.common.cryptography.CryptographicService;
import bd.com.squarehealth.corelibrary.common.cryptography.CryptographicServiceImpl;
import bd.com.squarehealth.corelibrary.common.json.JsonSerializer;
import bd.com.squarehealth.corelibrary.common.json.JsonSerializerImpl;
import io.jsonwebtoken.*;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JsonWebTokenServiceImpl implements JsonWebTokenService {

    private String accessTokenPrivateKeyAsString;
    private PrivateKey accessTokenPrivateKey;
    private String accessTokenPublicKeyAsString;
    private PublicKey accessTokenPublicKey;
    private String refreshTokenPrivateKeyAsString;
    private PrivateKey refreshTokenPrivateKey;
    private String refreshTokenPublicKeyAsString;
    private PublicKey refreshTokenPublicKey;

    private final JsonSerializer jsonSerializer = JsonSerializerImpl.getInstance();
    private final CryptographicService cryptographicService = new CryptographicServiceImpl();
    private static final String TOKEN_TYPE = "bearer";
    private static final int MINIMUM_AUTHORIZATION_HEADER_VALUE_LENGTH = "bearer x".length();
    private static final String ISSUER = "Square Health Ltd.";
    private static final String ACCESS_TOKEN_PRIVATE_KEY_RESOURCE_NAME = "access-token-private-key";
    private static final String ACCESS_TOKEN_PUBLIC_KEY_RESOURCE_NAME = "access-token-public-key";
    private static final String REFRESH_TOKEN_PRIVATE_KEY_RESOURCE_NAME = "refresh-token-private-key";
    private static final String REFRESH_TOKEN_PUBLIC_KEY_RESOURCE_NAME = "refresh-token-public-key";

    @Override
    public String getAccessTokenPrivateKeyAsString() {
        return accessTokenPrivateKeyAsString;
    }

    @Override
    public PrivateKey getAccessTokenPrivateKey() {
        if (accessTokenPrivateKey == null) {
            String accessTokenPrivateKeyAsString = ResourceUtilities.readResourceAsString(ACCESS_TOKEN_PRIVATE_KEY_RESOURCE_NAME);

            try {
                accessTokenPrivateKey = cryptographicService.parseRsaPrivateKey(accessTokenPrivateKeyAsString);
                this.accessTokenPrivateKeyAsString = accessTokenPrivateKeyAsString;
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        return accessTokenPrivateKey;
    }

    @Override
    public String getAccessTokenPublicKeyAsString() {
        return accessTokenPublicKeyAsString;
    }

    @Override
    public PublicKey getAccessTokenPublicKey() {
        if (accessTokenPublicKey == null) {
            String accessTokenPublicKeyAsString = ResourceUtilities.readResourceAsString(ACCESS_TOKEN_PUBLIC_KEY_RESOURCE_NAME);

            try {
                accessTokenPublicKey = cryptographicService.parseRsaPublicKey(accessTokenPublicKeyAsString);
                this.accessTokenPublicKeyAsString = accessTokenPublicKeyAsString;
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        return accessTokenPublicKey;
    }

    @Override
    public String getRefreshTokenPrivateKeyAsString() {
        return refreshTokenPrivateKeyAsString;
    }

    @Override
    public PrivateKey getRefreshTokenPrivateKey() {
        if (refreshTokenPrivateKey == null) {
            String refreshTokenPrivateKeyAsString = ResourceUtilities.readResourceAsString(REFRESH_TOKEN_PRIVATE_KEY_RESOURCE_NAME);

            try {
                refreshTokenPrivateKey = cryptographicService.parseRsaPrivateKey(refreshTokenPrivateKeyAsString);
                this.refreshTokenPrivateKeyAsString = refreshTokenPrivateKeyAsString;
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        return refreshTokenPrivateKey;
    }

    @Override
    public String getRefreshTokenPublicKeyAsString() {
        return refreshTokenPublicKeyAsString;
    }

    @Override
    public PublicKey getRefreshTokenPublicKey() {
        if (refreshTokenPublicKey == null) {
            String refreshTokenPublicKeyAsString = ResourceUtilities.readResourceAsString(REFRESH_TOKEN_PUBLIC_KEY_RESOURCE_NAME);

            try {
                refreshTokenPublicKey = cryptographicService.parseRsaPublicKey(refreshTokenPublicKeyAsString);
                this.refreshTokenPublicKeyAsString = refreshTokenPublicKeyAsString;
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        return refreshTokenPublicKey;
    }

    @Override
    public String getTokenType() {
        return TOKEN_TYPE;
    }

    @Override
    public String extractTokenFromAuthorizationHeader(String authorizationHeaderValue) {
        if (authorizationHeaderValue == null) { return null; }

        authorizationHeaderValue = authorizationHeaderValue.trim();

        if (authorizationHeaderValue.length() < MINIMUM_AUTHORIZATION_HEADER_VALUE_LENGTH) { return null; }

        String[] splittedAuthorizationHeaderValue = authorizationHeaderValue.split(" ");

        if (splittedAuthorizationHeaderValue.length != 2) { return null; }

        String tokenType = splittedAuthorizationHeaderValue[0].toLowerCase();

        // checks if token type is not "bearer"...
        if (!getTokenType().equals(tokenType)) { return null; }

        String token = splittedAuthorizationHeaderValue[1];

        return token;
    }

    @Override
    public <Type> String generateToken(Type payload, Integer expirationInMinutes, PrivateKey rsaPrivateKey) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("data", payload);

        JwtBuilder jwtBuilder = Jwts.builder()
                .setClaims(claims)
                .setIssuer(ISSUER)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .signWith(rsaPrivateKey, SignatureAlgorithm.RS512);

        if (expirationInMinutes != null) {
            int expirationInMilliseconds = expirationInMinutes * 60 * 1000;

            jwtBuilder.setExpiration(new Date(System.currentTimeMillis() + expirationInMilliseconds));
        }

        return jwtBuilder.compact();
    }

    @Override
    public <Type> Type validateToken(String token, PublicKey rsaPublicKey, Class<Type> classType) throws Exception {
        Jws<Claims> claimsJws = Jwts.parserBuilder()
                .setSigningKey(rsaPublicKey).build()
                .parseClaimsJws(token);

        return claimsJws.getBody().get("data", classType);
    }

    @Override
    public <Type> String generateAccessToken(Type payload, Integer expirationInMinutes) {
        return generateToken(payload, expirationInMinutes, getAccessTokenPrivateKey());
    }

    @Override
    public <Type> String generateAccessToken(Type payload) {
        return generateAccessToken(payload, null);
    }

    @Override
    public <Type> String generateRefreshToken(Type payload, Integer expirationInMinutes) {
        return generateToken(payload, expirationInMinutes, getRefreshTokenPrivateKey());
    }

    @Override
    public <Type> String generateRefreshToken(Type payload) {
        return generateRefreshToken(payload, null);
    }

    @Override
    public <Type> Type validateAccessToken(String accessToken, Class<Type> classType) throws Exception {
        return validateToken(accessToken, getAccessTokenPublicKey(), classType);
    }

    @Override
    public <Type> Type validateRefreshToken(String refreshToken, Class<Type> classType) throws Exception {
        return validateToken(refreshToken, getRefreshTokenPublicKey(), classType);
    }
}
