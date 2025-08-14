package vortex.imwp.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import vortex.imwp.services.LoginAuditService;
//import vortex.imwp.services.LogoutAuditService;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;

//@Component
//public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
//
//    private final LoginAuditService loginSvc;
//    private final LogoutAuditService logoutSvc;
//
//    public CustomLogoutSuccessHandler(LoginAuditService loginSvc, LogoutAuditService logoutSvc) {
//        this.loginSvc = loginSvc;
//        this.logoutSvc = logoutSvc;
//    }
//
//    @Override
//    public void onLogoutSuccess(HttpServletRequest request,
//                                HttpServletResponse response,
//                                Authentication authentication) throws IOException, ServletException {
//
//        HttpSession session = request.getSession(false);
//        Long loginAuditId = session != null ? (Long) session.getAttribute("LOGIN_AUDIT_ID") : null;
//        System.out.println("Login audit ID " + loginAuditId);
//        String username = authentication != null ? authentication.getName()
//                : session != null ? (String) session.getAttribute("EMPLOYEE_USERNAME")
//                : "unknown";
//        String ip = request.getRemoteAddr();
//        Timestamp now = new Timestamp(System.currentTimeMillis());
//
//        // вычислим длительность, если есть login запись
////        Long duration = null;
////        if (loginAuditId != null) {
////            Instant logoutInstant = now.toInstant();
////            Long seconds = loginSvc.calculateDurationSeconds(loginAuditId, logoutInstant); // реализация на сервисе
////            duration = seconds;
////        }
//
//        logoutSvc.recordLogout(username, ip, now, "MANUAL");
//
//        if (session != null) session.invalidate();
//        response.sendRedirect("/login?logout");
//    }
//}
