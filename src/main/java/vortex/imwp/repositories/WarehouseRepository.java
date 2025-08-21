package vortex.imwp.repositories;

import org.springframework.stereotype.Repository;
import vortex.imwp.models.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {

}