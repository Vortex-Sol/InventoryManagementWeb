package vortex.imwp.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vortex.imwp.Models.Report;

public interface ReportRepository extends JpaRepository<Report, Long> {
}
