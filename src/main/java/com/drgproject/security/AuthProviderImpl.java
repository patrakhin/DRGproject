package com.drgproject.security;

import com.drgproject.service.PersonDetailService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collections;


@Component
public class AuthProviderImpl implements AuthenticationProvider {

    private final PersonDetailService personDetailService;

    public AuthProviderImpl(PersonDetailService personDetailService) {
        this.personDetailService = personDetailService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userName = authentication.getName();
        UserDetails userDetails = personDetailService.loadUserByUsername(userName);
        String password = authentication.getCredentials().toString();
        if (!password.equals(userDetails.getPassword())){
            throw new BadCredentialsException("Incorrect password!");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, password, Collections.emptyList());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
