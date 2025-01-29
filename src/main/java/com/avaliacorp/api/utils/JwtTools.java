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
import com.avaliacorp.api.models.TokenModel;

import jakarta.annotation.PostConstruct;

@Component//Classe para gerar e decodificar token JWT
public class JwtTools {

    @Value("${JWT_SECRET}")
    private String secret;//Variável secret guarda o segredo definido na variável de ambiente JWT_SECRET
    private Algorithm algorithm;//Váriavel que guarda o algoritmo gerado para encriptação e descriptografia
    private JWTVerifier verifier;//Objeto que guarda funções para verificar e descriptografar tokens JWT

    @PostConstruct//Indica que o método deve ser chamado apenas após o objeto ter sido construido (para evitar erros por conta da tag Value)
    public void init(){
        this.algorithm = Algorithm.HMAC256(secret);//Cria o algoritmo baseado no HMAC256
        this.verifier = JWT.require(algorithm).build();//Cria o verifier
    }

    //Método para gerar os tokens
    public String genJwt(String value, TypeOfUser typeOfUser){
        Calendar calendar = Calendar.getInstance();//Cria um calendário, uma classe que tem funções para lidar com o tempo
        calendar.add(Calendar.HOUR, 24);//Pega a data e a hora daqui a 24 horas (data de expiração do token)
        Date expiresAt = calendar.getTime();//Cria o objeto data
        return JWT.create()
        .withClaim("id", value)//Cria um claim, uma propriedade do token, chamada id com o valor passado (id do usuário seja comum ou firma)
        .withClaim("type",typeOfUser.toString())//Cria outro claim chamado type que guarda um TypeOfUser, enum para os tipos de usuários
        .withJWTId(UUID.randomUUID().toString())//Cria um id pro JWT (+ Segurança)
        .withExpiresAt(expiresAt)//Seta a data de expiração do token com a data criada logo acima, 24 horas depois do token ser gerado
        .sign(algorithm);//Assina com o token com algoritmo de criptografia e verificação inicializado na função init();
    }

    public DecodedJWT verifyToken(String token){
        return verifier.verify(token);//Decodifica um token e retorna um DecodedJWT
    }

    public TokenModel verifyAndDecodeToken(String token){//Classe para decodificar o token e formata-lo pro formato TokenModel
        var decoded = verifyToken(token);
        return TokenModel.formatDecodedToken(decoded);
    }

}
