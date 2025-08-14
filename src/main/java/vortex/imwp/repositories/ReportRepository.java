package vortex.imwp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vortex.imwp.models.Report;

public interface ReportRepository extends JpaRepository<Report, Long> {
}
