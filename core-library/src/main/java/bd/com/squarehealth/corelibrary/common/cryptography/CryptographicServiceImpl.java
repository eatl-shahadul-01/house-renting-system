package bd.com.squarehealth.corelibrary.common.cryptography;

import bd.com.squarehealth.corelibrary.common.ResourceUtilities;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class CryptographicServiceImpl implements CryptographicService {

    private static final String MAC_ALGORITHM_NAME = "HmacSHA512";
    private static final String RSA_ALGORITHM_NAME = "RSA";

    @Override
    public String calculateHmacSha512(String message, String secretKey) throws Exception {
        Mac hmacSha512 = Mac.getInstance(MAC_ALGORITHM_NAME);
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), MAC_ALGORITHM_NAME);
        hmacSha512.init(secretKeySpec);
        byte[] messageDigestAsByteArray = hmacSha512.doFinal(message.getBytes(StandardCharsets.UTF_8));
        String messageDigest = Base64.getEncoder().encodeToString(messageDigestAsByteArray);

        return messageDigest;
    }

    @Override
    public PrivateKey parseRsaPrivateKey(String privateKeyAsString) throws Exception {
        privateKeyAsString = privateKeyAsString
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+","");
        byte[] encodedPrivateKeyAsString = Base64.getDecoder().decode(privateKeyAsString);
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(encodedPrivateKeyAsString);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM_NAME);
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);

        return privateKey;
    }

    @Override
    public PublicKey parseRsaPublicKey(String publicKeyAsString) throws Exception {
        publicKeyAsString = publicKeyAsString
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s+","");
        byte[] encodedPublicKeyAsString = Base64.getDecoder().decode(publicKeyAsString);
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(encodedPublicKeyAsString);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM_NAME);
        PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);

        return publicKey;
    }
}
