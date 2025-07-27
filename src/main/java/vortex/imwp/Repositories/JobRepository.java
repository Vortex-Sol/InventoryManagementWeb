package vortex.imwp.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vortex.imwp.Models.Job;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
}
