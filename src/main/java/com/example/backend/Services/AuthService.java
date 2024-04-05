package com.example.backend.Services;

import com.example.backend.Models.LoginModel;
import com.example.backend.Models.LoginResponseModel;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    public LoginResponseModel loginUser(LoginModel loginModel){
        LoginResponseModel response =new LoginResponseModel();
        response.setAccesstoken("jghfhfxjhkl;");
        response.setType(loginModel.getUsername());
        response.setName(loginModel.getUsername());

        return response;
    }
}
