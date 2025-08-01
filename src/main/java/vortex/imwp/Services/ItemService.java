package vortex.imwp.Services;

import vortex.imwp.DTOs.ItemDTO;
import vortex.imwp.Mappers.ItemDTOMapper;
import vortex.imwp.Models.Item;
import vortex.imwp.Models.Warehouse;
import vortex.imwp.Models.WarehouseItem;
import vortex.imwp.Repositories.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vortex.imwp.Repositories.WarehouseItemRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ItemService {
    private final ItemRepository itemRepository;
    private final WarehouseItemRepository warehouseItemRepository;

    public ItemService(ItemRepository itemRepository, WarehouseItemRepository warehouseItemRepository) {
        this.itemRepository = itemRepository;
        this.warehouseItemRepository = warehouseItemRepository;
    }
    public List<ItemDTO> getAll() {
        Iterable<Item> list = itemRepository.findAll();
        List<ItemDTO> items = new ArrayList<>();

        for (Item item : list) {
            ItemDTO dto = ItemDTOMapper.map(item);
            items.add(dto);
        }

        return items;
    }
    public List<ItemDTO> searchAndMap(String keyword) {
        List<Item> items = itemRepository.findByNameContainingIgnoreCase(keyword);
        List<ItemDTO> dtos = new ArrayList<>();

        for (Item item : items) {
            ItemDTO dto = ItemDTOMapper.map(item);
            List<WarehouseItem> warehouseItems = warehouseItemRepository.findByItem(item);

            int totalQty = warehouseItems.stream()
                    .mapToInt(WarehouseItem::getQuantityInStock)
                    .sum();

            warehouseItems.forEach(wi -> dto.addWarehouse(wi.getWarehouse()));

            dtos.add(dto);
        }

        return dtos;
    }

    public Item updateItem(ItemDTO dto) {
        Item item = ItemDTOMapper.map(dto);
        itemRepository.save(item);
        warehouseItemRepository.deleteByItem(item);
        for (Warehouse wh : dto.getWarehouses()) {
            //fake quantity
            WarehouseItem wi = new WarehouseItem(wh, item,0);
            warehouseItemRepository.save(wi);
        }

        return item;
    }
    public ItemDTO mapToDTO(Item item) {
        ItemDTO dto = ItemDTOMapper.map(item);
        List<WarehouseItem> warehouseItems = warehouseItemRepository.findByItem(item);
        int totalQty = warehouseItems.stream().mapToInt(WarehouseItem::getQuantityInStock).sum();
        warehouseItems.forEach(wi -> dto.addWarehouse(wi.getWarehouse()));
        return dto;
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