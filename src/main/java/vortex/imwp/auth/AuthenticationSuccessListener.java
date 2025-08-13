package vortex.imwp.auth;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;
import vortex.imwp.services.LoginAuditService;

import java.sql.Timestamp;

@Component
public class AuthenticationSuccessListener
        implements ApplicationListener<AuthenticationSuccessEvent> {

    private final LoginAuditService auditSvc;
    private final HttpServletRequest request;

    public AuthenticationSuccessListener(LoginAuditService auditSvc,
                                         HttpServletRequest request) {
        this.auditSvc = auditSvc;
        this.request  = request;
    }

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent ev) {
        String username = ev.getAuthentication().getName();
        String ip       = request.getRemoteAddr();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        System.out.println("[" + timestamp + "] Success: username: " + username + " ip: " + ip);

        auditSvc.recordLogin(username, ip, timestamp, true);
    }
}