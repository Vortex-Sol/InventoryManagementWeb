package vortex.imwp.mappers;

import vortex.imwp.dtos.LogoutAuditDTO;
import vortex.imwp.models.LogoutAudit;

public class LogoutAuditDTOMapper {
    public static LogoutAuditDTO map(LogoutAudit logoutAudit) {
        return new LogoutAuditDTO(
                logoutAudit.getId(),
                logoutAudit.getUsername(),
                logoutAudit.getIpAddress(),
                logoutAudit.getLogoutTime(),
                logoutAudit.getLogoutReason(),
                EmployeeDTOMapper.map(logoutAudit.getEmployee())
        );
    }

    public static LogoutAudit map(LogoutAuditDTO logoutAuditDTO) {
        return new LogoutAudit(
                logoutAuditDTO.getId(),
                logoutAuditDTO.getUsername(),
                logoutAuditDTO.getIpAddress(),
                logoutAuditDTO.getLogoutTime(),
                logoutAuditDTO.getLogoutReason(),
                EmployeeDTOMapper.map(logoutAuditDTO.getEmployee())
        );
    }
}
