package vortex.imwp.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vortex.imwp.Models.Receipt;

public interface ReceiptRepository extends JpaRepository<Receipt, Long> {

}
