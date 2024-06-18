package com.drgproject.config;

import com.drgproject.security.AuthProviderImpl;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthProviderImpl authProvider;

    public SecurityConfig(AuthProviderImpl authProvider) {
        this.authProvider = authProvider;
    }

    //Настройка аутентификации
    protected void configure(AuthenticationManagerBuilder auth){
        auth.authenticationProvider(authProvider);
    }
}
