package vortex.imwp.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vortex.imwp.Models.LoginAudit;

@Repository
public interface LoginAuditRepository extends JpaRepository<LoginAudit, Long> {
}
