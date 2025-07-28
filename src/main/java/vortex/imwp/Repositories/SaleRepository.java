package vortex.imwp.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vortex.imwp.Models.Sale;
import java.sql.Timestamp;
import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {
    //TODO: Fix - Program does not compile
    //List<Sale> findByTimestampBetweenOrderByTimestampAsc(Timestamp start, Timestamp end);
}
