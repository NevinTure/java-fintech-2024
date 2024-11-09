package edu.java.kudagoapi.security;

import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import edu.java.kudagoapi.deserializers.TokenCookieJweStringDeserializer;
import edu.java.kudagoapi.serializers.TokenCookieJweStringSerializer;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TokenCookieJweConfig {

    @Value("${app.token-authentication.cookie-token-key}")
    private String cookieTokenKey;

    @Bean
    @SneakyThrows
    public TokenCookieJweStringSerializer tokenCookieJweStringSerializer() {
        return new TokenCookieJweStringSerializer(new DirectEncrypter(
                OctetSequenceKey.parse(cookieTokenKey)
        ));
    }

    @Bean
    @SneakyThrows
    public TokenCookieJweStringDeserializer tokenCookieJweStringDeserializer() {
        return new TokenCookieJweStringDeserializer(new DirectDecrypter(
                OctetSequenceKey.parse(cookieTokenKey)
        ));
    }
}
