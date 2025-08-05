package vortex.imwp.Repositories;

import vortex.imwp.Models.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByNameContainingIgnoreCase(String keyword);
    List<Item> findAll();
    Optional<Item> findBySku(String sku);
    Optional<Item> findByBarcode(Long itemBarcode);
}
