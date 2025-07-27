package vortex.imwp.Services;

import vortex.imwp.DTOs.ItemDTO;
import vortex.imwp.Mappers.ItemDTOMapper;
import vortex.imwp.Models.Item;
import vortex.imwp.Repositories.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ItemService {
    private final ItemRepository itemRepository;
    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Optional<List<ItemDTO>> getAll(){
        Iterable<Item> list = itemRepository.findAll();
        List<ItemDTO> items = new ArrayList<>();
        if (list.iterator().hasNext()) {
            for (Item item : list) items.add(ItemDTOMapper.map(item));
            return Optional.of(items);
        }
        return Optional.empty();
    }

    public Optional<Item> getItemById(Long id) {
        return itemRepository.findById(id);
    }

    public Item addItem(ItemDTO item) {
        return itemRepository.save(ItemDTOMapper.map(item));
    }

    public Item updateItem(Item item) {
        return itemRepository.save(item);
    }

    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }

    public List<Item> searchItems(String keyword) {
        return itemRepository.findByNameContainingIgnoreCase(keyword);
    }
}