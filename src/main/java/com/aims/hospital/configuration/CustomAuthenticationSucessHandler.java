package com.aims.hospital.configuration;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationSucessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String role = authentication.getAuthorities().iterator().next().getAuthority();
        String url = "";
        switch (role){
            case "ROLE_PATIENT" :
                url = "/patient/dashboard";
                break;
            case "ROLE_DOCTOR":
                url = "/doctor/dashboard";
                break;
            case "ROLE_ADMIN":
                url = "/admin/dashboard";
                break;
        }
        response.sendRedirect(url);
    }
}
