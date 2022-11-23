package bd.com.squarehealth.corelibrary.common.security;

import java.security.PrivateKey;
import java.security.PublicKey;

public interface JsonWebTokenService {

    String getAccessTokenPrivateKeyAsString();

    PrivateKey getAccessTokenPrivateKey();

    String getAccessTokenPublicKeyAsString();

    PublicKey getAccessTokenPublicKey();

    String getRefreshTokenPrivateKeyAsString();

    PrivateKey getRefreshTokenPrivateKey();

    String getRefreshTokenPublicKeyAsString();

    PublicKey getRefreshTokenPublicKey();

    String getTokenType();

    String extractTokenFromAuthorizationHeader(String authorizationHeaderValue);

    <Type> String generateToken(Type payload, Integer expirationInMinutes, PrivateKey rsaPrivateKey);

    <Type> Type validateToken(String token, PublicKey rsaPublicKey, Class<Type> classType) throws Exception;

    <Type> String generateAccessToken(Type payload, Integer expirationInMinutes);

    <Type> String generateAccessToken(Type payload);

    <Type> String generateRefreshToken(Type payload, Integer expirationInMinutes);

    <Type> String generateRefreshToken(Type payload);

    <Type> Type validateAccessToken(String accessToken, Class<Type> classType) throws Exception;

    <Type> Type validateRefreshToken(String refreshToken, Class<Type> classType) throws Exception;
}
