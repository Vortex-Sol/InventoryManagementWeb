package vortex.imwp.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vortex.imwp.Models.Employee;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByUsername(String username);

    @Query("SELECT e FROM Employee e LEFT JOIN FETCH e.jobs WHERE e.username = :username")
    Optional<Employee> findByUsernameWithJobs(@Param("username") String username);
}
