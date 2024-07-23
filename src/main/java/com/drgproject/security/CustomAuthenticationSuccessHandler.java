package com.drgproject.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Collection;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals("Администратор") || authority.getAuthority().equals("Регионал")) {
                response.sendRedirect("/navigation_bar"); //Направляются на эту страницу
                return;
            } else if (authority.getAuthority().equals("Бригадир") || authority.getAuthority().equals("Электрик")) {
                response.sendRedirect("/repair_history/search"); //Направляются на эту страницу
                return;
            }
        }
        // Если роль не найдена, перенаправляем на страницу ошибки
        response.sendRedirect("/login?error=true");
    }
}
