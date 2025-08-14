package vortex.imwp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vortex.imwp.models.Receipt;

public interface ReceiptRepository extends JpaRepository<Receipt, Long> {

}
