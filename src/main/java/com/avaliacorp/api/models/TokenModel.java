package com.avaliacorp.api.models;

import java.util.Date;

import com.auth0.jwt.interfaces.DecodedJWT;

import lombok.Data;

@Data
public class TokenModel {

    private Date expiresAt;
    private String type;
    private String id;

    public TokenModel(String id, String type, Date expiresAt){
        this.id = id;
        this.type = type;
        this.expiresAt = expiresAt;
    }

    public static TokenModel formatDecodedToken(DecodedJWT decoded){
        if(decoded == null){
            throw new NullPointerException("Decoded param must not be null");
        }
        String id = decoded.getClaim("id").toString().replace("\"", "");
        String type = decoded.getClaim("type").toString().replace("\"", "");
        Date expiresAt = decoded.getExpiresAt();
        return new TokenModel(id, type, expiresAt);
    }

}
