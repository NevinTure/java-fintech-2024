package edu.java.kudagoapi.deserializers;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEDecrypter;
import com.nimbusds.jwt.EncryptedJWT;
import com.nimbusds.jwt.JWTClaimsSet;
import edu.java.kudagoapi.utils.Token;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.text.ParseException;
import java.time.ZoneOffset;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class TokenCookieJweStringDeserializer {

    private final JWEDecrypter jweDecrypter;

    public Token deserialize(String s) {
        try {
            EncryptedJWT encryptedJWT = EncryptedJWT.parse(s);
            encryptedJWT.decrypt(jweDecrypter);
            JWTClaimsSet claimsSet = encryptedJWT.getJWTClaimsSet();
            return new Token(
                    UUID.fromString(claimsSet.getJWTID()),
                    claimsSet.getSubject(),
                    claimsSet.getStringListClaim("authorities"),
                    claimsSet.getIssueTime().toInstant().atOffset(ZoneOffset.UTC),
                    claimsSet.getExpirationTime().toInstant().atOffset(ZoneOffset.UTC));
        } catch (ParseException | JOSEException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }
}
