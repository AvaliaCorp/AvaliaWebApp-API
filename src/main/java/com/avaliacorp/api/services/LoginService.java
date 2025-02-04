package com.avaliacorp.api.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.avaliacorp.api.exceptions.LogInFailedException;
import com.avaliacorp.api.exceptions.NotFoundException;
import com.avaliacorp.api.models.EntityModel;
import com.avaliacorp.api.models.FirmModel;
import com.avaliacorp.api.models.UserModel;
import com.avaliacorp.api.repositories.FirmRepository;
import com.avaliacorp.api.repositories.UserRepository;
import com.avaliacorp.api.utils.JwtTools;
import com.avaliacorp.api.utils.TypeOfUser;

import at.favre.lib.crypto.bcrypt.BCrypt;

@Service//Define pro String que é um Service, também sub-entende que é um componente
public class LoginService {

    @Autowired
    private JwtTools jwtTools;

    private final UserRepository userRepository;
    private final FirmRepository firmRepository;

    public LoginService(UserRepository userRepository, FirmRepository firmRepository, JwtTools jwtTools){
        this.userRepository = userRepository;
        this.firmRepository = firmRepository;
        this.jwtTools = jwtTools;
        jwtTools.init();
    }

    private boolean comparePassword(String password, String hash){
        var verifyer = BCrypt.verifyer();
        var result = verifyer.verify(password.toCharArray(), hash.toCharArray());
        return result.verified;
    }

    public String login(String email, String password){

        Optional<UserModel> user = userRepository.findByEmail(email);

        if(user.isPresent()){
            if(comparePassword(password, user.get().getPassword())){
                if(user.get().getRole().equals("NormalUser")) return jwtTools.genJwt(user.get().getId(), TypeOfUser.NormalUser);
                if(user.get().getRole().equals("Admin")) return jwtTools.genJwt(user.get().getId(), TypeOfUser.Admin);
            }
            throw new LogInFailedException();
        }

        Optional<FirmModel> firm = firmRepository.findByEmail(email);

        if(firm.isPresent()){
            if(comparePassword(password, firm.get().getPassword())) {
                return jwtTools.genJwt(firm.get().getId(), TypeOfUser.Professional);
            }
            throw new LogInFailedException();
        }

        throw new NotFoundException("The user was not found, verify if the email is registered");

    }

    public EntityModel<String> getLoggedUser(String auth){
        var token = jwtTools.verifyAndDecodeToken(auth);
        if(token.getType().equals("NormalUser") || token.getType().equals("Admin")){
            return userRepository.findById(token.getId()).orElseThrow(() -> new NotFoundException("The User was not found"));
        }
        else {
            return firmRepository.findById(token.getId()).orElseThrow(() -> new NotFoundException("The Firm was not found"));
        }
    }

}
