package vortex.imwp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vortex.imwp.models.Job;

import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    public Job findById(long id);

    Optional<Job> findByName(String roleName);
}
