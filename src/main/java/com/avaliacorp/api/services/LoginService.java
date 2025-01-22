package com.avaliacorp.api.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.avaliacorp.api.ApiApplication;
import com.avaliacorp.api.controllers.AuthController.ResponseGetLoggedUser;
import com.avaliacorp.api.exceptions.LogInFailedException;
import com.avaliacorp.api.exceptions.NotFoundException;
import com.avaliacorp.api.models.FirmModel;
import com.avaliacorp.api.models.UserModel;
import com.avaliacorp.api.repositories.FirmRepository;
import com.avaliacorp.api.repositories.UserRepository;
import com.avaliacorp.api.utils.JwtTools;
import com.avaliacorp.api.utils.TypeOfUser;

import at.favre.lib.crypto.bcrypt.BCrypt;

@Service
public class LoginService {

    @Autowired
    private JwtTools jwtTools;

    private final UserRepository userRepository;
    private final FirmRepository firmRepository;

    public LoginService(UserRepository userRepository, FirmRepository firmRepository){
        this.userRepository = userRepository;
        this.firmRepository = firmRepository;
    }

    private boolean comparePassword(String password, String hash){
        var verifyer = BCrypt.verifyer();
        var result = verifyer.verify(password.toCharArray(), hash.toCharArray());
        return result.verified;
    }

    public String login(String email, String password){
        jwtTools.init();

        Optional<UserModel> user = userRepository.findByEmail(email);

        if(user.isPresent()){
            if(comparePassword(password, user.get().getPassword())){
                String token =  jwtTools.genJwt("id", user.get().getId(), TypeOfUser.NormalUser);
                ApiApplication.setToken(token);
                return token;
            }
            throw new LogInFailedException();
        }

        Optional<FirmModel> firm = firmRepository.findByEmail(email);

        if(firm.isPresent()){
            if(comparePassword(password, firm.get().getEmail())) {
                String token = jwtTools.genJwt(email, password, TypeOfUser.Professional);
                ApiApplication.setToken(token);
                return token;
            }
            throw new LogInFailedException();
        }

        throw new NotFoundException("The user was not found, verify if the email is registered");

    }

    public void logout(){
        ApiApplication.setToken("unlogged");
    }

    public ResponseGetLoggedUser getUserLogged(){
        var decoded = jwtTools.verifyToken(ApiApplication.getToken());
        String userId = decoded.getClaim("id").asString();
        String userType = decoded.getClaim("type").asString();
        return new ResponseGetLoggedUser(userId, userType);
    }
}
