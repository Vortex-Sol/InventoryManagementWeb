package vortex.imwp.mappers;

import vortex.imwp.dtos.LoginAuditDTO;
import vortex.imwp.models.LoginAudit;

public class LoginAuditDTOMapper {
    public static LoginAuditDTO map(LoginAudit loginAudit) {
        return new LoginAuditDTO(
                loginAudit.getId(),
                loginAudit.getUsername(),
                loginAudit.getIpAddress(),
                loginAudit.getLoginTime(),
                loginAudit.getSuccessFailure(),
                EmployeeDTOMapper.map(loginAudit.getEmployee())
        );
    }

    public static LoginAudit map(LoginAuditDTO loginAuditDTO) {
        return new LoginAudit(
                loginAuditDTO.getId(),
                loginAuditDTO.getUsername(),
                loginAuditDTO.getIpAddress(),
                loginAuditDTO.getLoginDate(),
                loginAuditDTO.getSuccessFailure(),
                EmployeeDTOMapper.map(loginAuditDTO.getEmployee())
        );
    }
}
