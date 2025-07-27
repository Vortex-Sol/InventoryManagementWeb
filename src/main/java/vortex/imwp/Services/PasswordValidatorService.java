package vortex.imwp.Services;

import org.springframework.stereotype.Service;

@Service
public class PasswordValidatorService {
    public boolean validatePassword(String password) {
        if (password.length() < 8) return false;
        if (!hasDigit(password)) return false;
        if (!hasLowercaseLetter(password)) return false;
        if (!hasCapitalLetter(password)) return false;
        return hasSpecialChar(password);
    }

    private boolean hasDigit(String password) {
        for (int i = 0; i < password.length(); i++) {
            if (Character.isDigit(password.charAt(i))) return true;
        }
        return false;
    }
    private boolean hasLowercaseLetter(String password) {
        for (int i = 0; i < password.length(); i++) if (Character.isLowerCase(password.charAt(i))) return true;
        return false;
    }
    private boolean hasCapitalLetter(String password) {
        for (int i = 0; i < password.length(); i++) if (Character.isUpperCase(password.charAt(i))) return true;
        return false;
    }
    private boolean hasSpecialChar(String password) {
        for (int i = 0; i < password.length(); i++) {
            if (!Character.isLetterOrDigit(password.charAt(i))) return true;
        }
        return false;
    }
}
