package bd.com.squarehealth.corelibrary.common.cryptography;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Commands to generate RSA key pair-
 * ssh-keygen -t rsa -b 4096 -C "house-renting-system@squarehealth.com.bd"
 * ssh-keygen -p -N "" -m pem -f [private-key]
 * openssl pkcs8 -topk8 -nocrypt -in [private-key] -out [private-key].pk8
 * ssh-keygen -f [private-key].pk8 -e -m pkcs8 > [public-key].pk8
 * openssl rsa -pubin -in [public-key].pk8 -outform pem > [public-key].pem
 */
public interface CryptographicService {

    String calculateHmacSha512(String message, String secretKey) throws Exception;

    PrivateKey parseRsaPrivateKey(String privateKeyAsString) throws Exception;

    PublicKey parseRsaPublicKey(String publicKeyAsString) throws Exception;
}
