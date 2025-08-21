package vortex.imwp.repositories;

import org.springframework.stereotype.Repository;
import vortex.imwp.models.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByNameContainingIgnoreCase(String keyword);
    List<Item> findAll();
    Optional<Item> findItemByBarcode(Long itemBarcode);
}
