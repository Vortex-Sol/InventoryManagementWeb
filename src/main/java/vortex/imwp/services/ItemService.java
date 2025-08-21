package vortex.imwp.services;

import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import vortex.imwp.dtos.CategoryDTO;
import vortex.imwp.dtos.ItemDTO;
import vortex.imwp.mappers.ItemDTOMapper;
import vortex.imwp.models.*;
import vortex.imwp.repositories.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ItemService {
    private final ItemRepository itemRepository;
    private final WarehouseItemRepository warehouseItemRepository;
    private final CategoryRepository categoryRepository;
    private final SaleItemRepository saleItemRepository;
    private final EmployeeService employeeService;
    private final ItemChangeAuditRepository itemChangeAuditRepository;

    public ItemService(ItemRepository itemRepository, WarehouseItemRepository warehouseItemRepository,
                       CategoryRepository categoryRepository, SaleItemRepository saleItemRepository, EmployeeService employeeService, ItemChangeAuditRepository itemChangeAuditRepository) {
        this.itemRepository = itemRepository;
        this.warehouseItemRepository = warehouseItemRepository;
        this.categoryRepository = categoryRepository;
        this.saleItemRepository = saleItemRepository;
        this.employeeService = employeeService;
        this.itemChangeAuditRepository = itemChangeAuditRepository;
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

    public Item addItem(ItemDTO dto, Authentication authentication, int quantity) {
        Item item = ItemDTOMapper.map(dto);
        Long catId = Optional.ofNullable(dto.getCategory())
                .map(CategoryDTO::getId)
                .orElseThrow(() -> new IllegalArgumentException("Category is required"));
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found: " + catId));
        item.setCategory(category);

        Item savedItem = itemRepository.save(item);

        Employee stocker = employeeService.getEmployeeByAuthentication(authentication);
        String dataChanged = "Item: id(" + savedItem.getId() + ") \"" + savedItem.getName() + "\" (category: "+ category.getName() + ") was added. Quantity: " + quantity;
        ItemChangeAudit audit = new ItemChangeAudit(savedItem.getId(), stocker.getId(), stocker.getWarehouseID(), LocalDateTime.now(), dataChanged);
        itemChangeAuditRepository.save(audit);

        return savedItem;
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

    public Item updateItem(Item newItem, Authentication authentication) {


        Optional<Item> oldItem_check = itemRepository.findById(newItem.getId());
        if (oldItem_check.isEmpty()) {
            return null;
        }
        Item oldItem = oldItem_check.get();

        StringBuilder sb = new StringBuilder();

        // --- Name (String) ---
        if (!Objects.equals(oldItem.getName(), newItem.getName())) {
            sb.append("Name: ")
                    .append(oldItem.getName() == null ? "null" : oldItem.getName())
                    .append(" - ")
                    .append(newItem.getName() == null ? "null" : newItem.getName())
                    .append("; ");
            oldItem.setName(newItem.getName());
        }

        // --- Description (String) ---
        if (!Objects.equals(oldItem.getDescription(), newItem.getDescription())) {
            sb.append("Description: ")
                    .append(oldItem.getDescription() == null ? "null" : oldItem.getDescription())
                    .append(" - ")
                    .append(newItem.getDescription() == null ? "null" : newItem.getDescription())
                    .append("; ");
            oldItem.setDescription(newItem.getDescription());
        }

        // --- Price (Double) ---
        if (!Objects.equals(oldItem.getPrice(), newItem.getPrice())) {
            sb.append("Price: ")
                    .append(oldItem.getPrice() == null ? "null" : oldItem.getPrice().toString())
                    .append(" - ")
                    .append(newItem.getPrice() == null ? "null" : newItem.getPrice().toString())
                    .append("; ");
            oldItem.setPrice(newItem.getPrice());
        }

        // --- Barcode (Long) ---
        if (!Objects.equals(oldItem.getBarcode(), newItem.getBarcode())) {
            sb.append("Barcode: ")
                    .append(oldItem.getBarcode() == null ? "null" : oldItem.getBarcode().toString())
                    .append(" - ")
                    .append(newItem.getBarcode() == null ? "null" : newItem.getBarcode().toString())
                    .append("; ");
            oldItem.setBarcode(newItem.getBarcode());
        }

        // --- Category (compare by id, show id and name) ---
        String oldCatName = oldItem.getCategory().getName() == null ? "null" : oldItem.getCategory().getName();
        String newCatName = newItem.getCategory().getName() == null ? "null" : newItem.getCategory().getName();
        if (!Objects.equals(oldCatName, newCatName)) {
            sb.append("Category: ")
                    .append(oldItem.getCategory().getName() == null ? "null" : oldCatName)
                    .append(" - ")
                    .append(newItem.getCategory().getName() == null ? "null" : newCatName)
                    .append("; ");
            oldItem.setCategory(newItem.getCategory());
        }

        Employee stocker = employeeService.getEmployeeByAuthentication(authentication);

        if (sb.isEmpty()) {
            return oldItem;
        }

        ItemChangeAudit audit = new ItemChangeAudit(
                oldItem.getId(),
                stocker.getId(),
                stocker.getWarehouseID(),
                LocalDateTime.now(),
                sb.toString());
        itemChangeAuditRepository.save(audit);

        return itemRepository.save(newItem);
    }

    @Transactional
    public void deleteItem(Long id, Authentication authentication) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Item not found: " + id));
        warehouseItemRepository.deleteByItem(item);
        saleItemRepository.deleteSaleItemByItemId(item.getId());

        Employee stocker = employeeService.getEmployeeByAuthentication(authentication);
        String dataChanged = "Item: id(" + item.getId() + ") \"" + item.getName() + "\" (category: "+ item.getCategory().getName() + ") was deleted.";
        ItemChangeAudit audit = new ItemChangeAudit(
                item.getId(),
                stocker.getId(),
                stocker.getWarehouseID(),
                LocalDateTime.now(),
                dataChanged);
        itemChangeAuditRepository.save(audit);

        itemRepository.delete(item);
    }

    public List<Item> searchItems(String keyword) {
        return itemRepository.findByNameContainingIgnoreCase(keyword);
    }
}