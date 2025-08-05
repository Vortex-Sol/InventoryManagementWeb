package vortex.imwp.Mappers;

import vortex.imwp.DTOs.LoginAuditDTO;
import vortex.imwp.Models.LoginAudit;

public class LoginAuditDTOMapper {
    public static LoginAuditDTO map(LoginAudit loginAudit) {
        return new LoginAuditDTO(
                loginAudit.getId(),
                loginAudit.getUsername(),
                loginAudit.getIpAddress(),
                loginAudit.getLoginTime(),
                loginAudit.getSuccessFailure(),
                loginAudit.getEmployee()
        );
    }

    public static LoginAudit map(LoginAuditDTO loginAuditDTO) {
        return new LoginAudit(
                loginAuditDTO.getId(),
                loginAuditDTO.getUsername(),
                loginAuditDTO.getIpAddress(),
                loginAuditDTO.getLoginDate(),
                loginAuditDTO.getSuccessFailure(),
                loginAuditDTO.getEmployee()
        );
    }
}
