package vortex.imwp.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vortex.imwp.dtos.EmployeeDTO;
import vortex.imwp.services.EmployeeService;
import vortex.imwp.services.LogoutAuditService;

import java.io.IOException;
import java.sql.Timestamp;


@Controller
@RequestMapping("/auth")
public class AuthController {

    private final LogoutAuditService logoutAuditService;

    public AuthController(LogoutAuditService logoutAuditService) {
        this.logoutAuditService = logoutAuditService;
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("user", new EmployeeDTO());
        return "auth/login";
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response, Authentication auth) {
        HttpSession session = request.getSession(false);
        Long loginAuditId = session != null ? (Long) session.getAttribute("LOGIN_AUDIT_ID") : null;
        String username = auth.getName();
        String ip = request.getRemoteAddr();
        Timestamp now = new Timestamp(System.currentTimeMillis());

        logoutAuditService.recordLogout(username, ip, now, "MANUAL");

        auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth); // инвалидирует сессию
        } else if (session != null) {
            session.invalidate();
        }

        return "redirect:/login?logout"; // единственный редирект
    }

}
