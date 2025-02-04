package com.avaliacorp.api.models;

public record LoginModel(String email, String password){

    public final boolean isNull(){
        return (email == null || password == null);
    }

}