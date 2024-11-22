package edu.java.kudagoapi.serializers;

import com.nimbusds.jose.*;
import com.nimbusds.jwt.*;
import edu.java.kudagoapi.utils.Token;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
public class TokenCookieJweStringSerializer {

    private final JWEEncrypter jweEncrypter;
    private final JWEAlgorithm jweAlgorithm = JWEAlgorithm.DIR;
    private final EncryptionMethod encryptionMethod = EncryptionMethod.A256GCM;


    public String serialize(Token token) {
        JWEHeader header = new JWEHeader.Builder(jweAlgorithm, encryptionMethod)
                .keyID(token.id().toString())
                .build();
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .jwtID(token.id().toString())
                .subject(token.subject())
                .issueTime(Date.from(token.createdAt().toInstant()))
                .expirationTime(Date.from(token.expiresAt().toInstant()))
                .claim("authorities", token.authorities())
                .build();
        EncryptedJWT encryptedJWT = new EncryptedJWT(header, claimsSet);
        try {
            encryptedJWT.encrypt(jweEncrypter);
            return encryptedJWT.serialize();
        } catch (JOSEException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }
}
