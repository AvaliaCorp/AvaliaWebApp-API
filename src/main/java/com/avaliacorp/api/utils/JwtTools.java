package com.avaliacorp.api.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import jakarta.annotation.PostConstruct;

@Component
public class JwtTools {

    @Value("${JWT_SECRET}")
    private String secret;
    private Algorithm algorithm;
    private JWTVerifier verifier;

    @PostConstruct
    public void init(){
        System.out.println("SECRET: " + secret);
        this.algorithm = Algorithm.HMAC256(secret);
        this.verifier = JWT.require(algorithm).build();
    }

    public String genJwt(String claimName, String value, TypeOfUser typeOfUser){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 24);
        Date expiresAt = calendar.getTime();
        return JWT.create()
        .withClaim("id", value)
        .withClaim("type",typeOfUser.toString())
        .withJWTId(UUID.randomUUID().toString())
        .withExpiresAt(expiresAt)
        .sign(algorithm);
    }

    public DecodedJWT verifyToken(String token){
        DecodedJWT decodedJWT = verifier.verify(token);
        return decodedJWT;
    }

}
