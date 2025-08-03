package vortex.imwp.Auth;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;
import vortex.imwp.Services.LoginAuditService;

import java.sql.Timestamp;

@Component
public class AuthenticationFailureListener
        implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    private final LoginAuditService auditSvc;
    private final HttpServletRequest request;

    public AuthenticationFailureListener(LoginAuditService auditSvc,
                                         HttpServletRequest request) {
        this.auditSvc = auditSvc;
        this.request  = request;
    }

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent ev) {
        String username = ev.getAuthentication().getName();
        String ip       = request.getRemoteAddr();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        System.out.println("[" + timestamp + "] Failure: username: " + username + " ip: " + ip);

        auditSvc.recordLogin(username, ip, timestamp, true);
    }
}