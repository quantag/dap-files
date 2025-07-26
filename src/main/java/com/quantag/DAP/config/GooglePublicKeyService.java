package com.quantag.DAP.config;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.security.interfaces.RSAPublicKey;

@Service
@Slf4j
public class GooglePublicKeyService {

    private static final String GOOGLE_CERTS_URL = "https://www.googleapis.com/oauth2/v3/certs";

    public RSAPublicKey getPublicKey(String kid) throws Exception {
        JWKSet jwkSet = JWKSet.load(new URL(GOOGLE_CERTS_URL));

        JWK jwk = jwkSet.getKeyByKeyId(kid);
        if (jwk == null) {
            log.error("ERROR in GooglePublicKeyService: No key found for KeyID: " + kid);
            throw new IllegalArgumentException("No key found for KeyID: " + kid);
        }

        return ((RSAKey) jwk).toRSAPublicKey();
    }

    public RSAPublicKey getPublicKeyFromToken(String token) throws Exception {
        SignedJWT signedJWT = SignedJWT.parse(token);
        String kid = signedJWT.getHeader().getKeyID();
        return getPublicKey(kid);
    }

}