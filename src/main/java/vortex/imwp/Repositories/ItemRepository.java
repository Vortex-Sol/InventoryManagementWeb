package vortex.imwp.Repositories;

import vortex.imwp.Models.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByNameContainingIgnoreCase(String keyword);
    //List<Item> findALl();
    List<Item> findAll();
}
