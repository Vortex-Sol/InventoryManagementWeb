package vortex.imwp.services;

import jakarta.transaction.Transactional;
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
import java.util.stream.Collectors;

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
        if (keyword == null || keyword.isBlank()) {
            return Collections.emptyList();
        }
        List<Item> items = itemRepository.findByNameContainingIgnoreCase(keyword.trim());

        List<ItemDTO> dtos = new ArrayList<>(items.size());
        for (Item item : items) {
            dtos.add(ItemDTOMapper.map(item));
        }

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
    public List<ItemDTO> getItemsByWarehouse(Long warehouseId) {
        var rows = warehouseItemRepository.findAllByWarehouseId(warehouseId);
        return rows.stream()
                .map(WarehouseItem::getItem)
                .distinct()
                .map(ItemDTOMapper::map)
                .toList();
    }

    public Map<Long, Integer> getQuantitiesForWarehouse(Long warehouseId) {
        var rows = warehouseItemRepository.findAllByWarehouseId(warehouseId);
        Map<Long, Integer> map = new HashMap<>();
        for (WarehouseItem wi : rows) {
            map.put(wi.getItem().getId(), wi.getQuantityInStock());
        }
        return map;
    }

    public List<ItemDTO> searchAndMapInWarehouse(String keyword, Long warehouseId) {
        if (keyword == null || keyword.isBlank()) return Collections.emptyList();

        var allowedItemIds = warehouseItemRepository.findAllByWarehouseId(warehouseId)
                .stream().map(wi -> wi.getItem().getId()).collect(Collectors.toSet());

        return itemRepository.findByNameContainingIgnoreCase(keyword.trim())
                .stream()
                .filter(i -> allowedItemIds.contains(i.getId()))
                .map(ItemDTOMapper::map)
                .toList();
    }

    public Optional<Item> getItemById(Long id) {
        return itemRepository.findById(id);
    }

    //SKU MOVED TO WarehouseItem
    //public Optional<Item> getItemBySku(String sku) { return this.itemRepository.findBySku(sku); }

    public Item updateItem(Item item) {
        return itemRepository.save(item);
    }

    @Transactional
    public void deleteItem(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Item not found: " + id));
        warehouseItemRepository.deleteByItem(item);
        categoryRepository.deleteById(item.getCategory().getId());

        itemRepository.delete(item);
    }

    public List<Item> searchItems(String keyword) {
        return itemRepository.findByNameContainingIgnoreCase(keyword);
    }
}