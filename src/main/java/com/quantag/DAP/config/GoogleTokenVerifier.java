package com.quantag.DAP.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.algorithms.Algorithm;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;

@Slf4j
public class GoogleTokenVerifier {

    public String verifyToken(String idToken) {
        String header = new String(Base64.getUrlDecoder().decode(idToken.split("\\.")[0]));
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode headerNode = null;

        try {
            headerNode = objectMapper.readTree(header);
        }
        catch (JsonProcessingException jsonProcessingException) {
            log.error("ERROR cannot read from token header: {}", jsonProcessingException.getMessage());
        }

        BigInteger modulus = null;
        BigInteger exponent = null;

        assert headerNode != null;
        String kid = headerNode.get("kid").asText();
        try {
            GooglePublicKeyService keyService = new GooglePublicKeyService();
            RSAPublicKey rsaPublicKey = keyService.getPublicKey(kid);

            modulus = rsaPublicKey.getModulus();
            exponent = rsaPublicKey.getPublicExponent();
        }
        catch (Exception ex) {
            log.error("ERROR in getPublicKey(): {}", ex.getMessage());
        }

        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(modulus, exponent);
        KeyFactory keyFactory = null;
        RSAPublicKey publicKey = null;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            log.error("ERROR get instance in key factory: {}", noSuchAlgorithmException.getMessage());
        }

        try {
            assert keyFactory != null;
            publicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);
        }
        catch (InvalidKeySpecException invalidKeySpecException) {
            log.error("ERROR in generating public key: {}", invalidKeySpecException.getMessage());
        }

        Algorithm algorithm = Algorithm.RSA256(publicKey);
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT jwt = verifier.verify(idToken);

        if(jwt == null) {
            log.error("ERROR in verification: JWT is null");
            return "null";
        }

//            String userId = jwt.getSubject();
//            String email = jwt.getClaim("email").asString();
//            String name = jwt.getClaim("name").asString();
//            String picture = jwt.getClaim("picture").asString();

        return jwt.getSubject();
    }
}
