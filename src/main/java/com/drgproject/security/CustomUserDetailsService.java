package com.drgproject.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.drgproject.entity.User;
import com.drgproject.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    private final UserRepository userRepository;
    private final HttpSession session;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository, HttpSession session) {
        this.userRepository = userRepository;
        this.session = session;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {

        logger.info("Loading user by login: {}", login);

        User user = userRepository.findUserByLogin(login)

                //User user = userRepository.findUserByFio(login)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        session.setAttribute("region", user.getRegion());
        session.setAttribute("post", user.getPost());
        session.setAttribute("unit", user.getUnit());
        session.setAttribute("number_table", user.getNumberTable());

        return new org.springframework.security.core.userdetails.User(
                user.getLogin(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(user.getPost()))
        );
    }
}
