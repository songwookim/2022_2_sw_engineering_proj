package com.example.finalprj.web.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserLogin {

    private String username;
    private String password;
    //private boolean rememberme;

}
