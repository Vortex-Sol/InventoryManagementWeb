package vortex.imwp.Auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

        if (roles.contains("ROLE_ADMIN")) response.sendRedirect("/api/admin");
        else if (roles.contains("ROLE_MANAGER")) response.sendRedirect("/api/manager");
        else if (roles.contains("ROLE_STOCKER")) response.sendRedirect("/api/home"); //TODO: Proper Controllers [IMPORTANT] home is kinda like the stocker dashboard (adding/editing/removing items)
        else response.sendRedirect("/api/salesman"); //
    }
}
