package vortex.imwp.services;

import vortex.imwp.dtos.CategoryDTO;
import vortex.imwp.dtos.ItemDTO;
import vortex.imwp.mappers.ItemDTOMapper;
import vortex.imwp.models.Category;
import vortex.imwp.models.Item;
import vortex.imwp.models.WarehouseItem;
import vortex.imwp.repositories.CategoryRepository;
import vortex.imwp.repositories.ItemRepository;
import org.springframework.stereotype.Service;
import vortex.imwp.repositories.WarehouseItemRepository;

import java.util.*;

@Service
public class ItemService {
    private final ItemRepository itemRepository;
    private final WarehouseItemRepository warehouseItemRepository;
    private final CategoryRepository categoryRepository;

    public ItemService(ItemRepository itemRepository, WarehouseItemRepository warehouseItemRepository, CategoryRepository categoryRepository) {
        this.itemRepository = itemRepository;
        this.warehouseItemRepository = warehouseItemRepository;
        this.categoryRepository = categoryRepository;
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

        //TODO: Fix based on new ItemDTO (DO NOT CHANGE MODEL & DTO CLASSES)
        /*for (Item item : items) {
            ItemDTO dto = ItemDTOMapper.map(item);
            List<WarehouseItem> warehouseItems = warehouseItemRepository.findByItem(item);

            int totalQty = warehouseItems.stream()
                    .mapToInt(WarehouseItem::getQuantityInStock)
                    .sum();

            dto.setQuantity(totalQty);
            warehouseItems.forEach(wi -> dto.addWarehouse(wi.getWarehouse()));

            dtos.add(dto);
        }*/

        return dtos;
    }

    public Map<Long, List<String>> getItemWarehousesMap() {
        Map<Long, List<String>> map = new HashMap<>();

        List<Item> items = itemRepository.findAll();
        for (Item item : items) {
            List<WarehouseItem> warehouseItems = warehouseItemRepository.findByItem(item);

            List<String> addresses = warehouseItems.stream()
                    .map(wi -> wi.getWarehouse().getAddress())
                    .distinct()
                    .toList();

            map.put(item.getId(), addresses);
        }

        return map;
    }


    public Optional<ItemDTO> getItemByBarcode(Long barcode) {
        Optional<Item> item = itemRepository.findItemByBarcode(barcode);
        System.out.println("Item: " + item);
        if (item.isPresent()) {
            ItemDTO dto = ItemDTOMapper.map(item.get());
            return Optional.of(dto);
        }
        return Optional.empty();
    }
    public Map<Long, Integer> getQuantitiesForAllItems() {
        Map<Long, Integer> itemQuantities = new HashMap<>();

        Iterable<Item> items = itemRepository.findAll();
        for (Item item : items) {
            List<WarehouseItem> warehouseItems = warehouseItemRepository.findByItem(item);
            int totalQty = warehouseItems.stream()
                    .mapToInt(WarehouseItem::getQuantityInStock)
                    .sum();

            itemQuantities.put(item.getId(), totalQty);
        }

        return itemQuantities;
    }

    public Item addItem(ItemDTO dto) {
        Item item = ItemDTOMapper.map(dto);
        Long catId = Optional.ofNullable(dto.getCategory())
                .map(CategoryDTO::getId)
                .orElseThrow(() -> new IllegalArgumentException("Category is required"));
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found: " + catId));
        item.setCategory(category);

        return itemRepository.save(item);
    }


    public Optional<Item> getItemById(Long id) {
        return itemRepository.findById(id);
    }

    //SKU MOVED TO WarehouseItem
    //public Optional<Item> getItemBySku(String sku) { return this.itemRepository.findBySku(sku); }

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