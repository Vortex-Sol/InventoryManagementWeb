package vortex.imwp.services;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import vortex.imwp.dtos.ItemDTO;
import vortex.imwp.dtos.WarehouseItemDTO;
import vortex.imwp.mappers.ItemDTOMapper;
import vortex.imwp.mappers.WarehouseItemDTOMapper;
import vortex.imwp.models.WarehouseItem;
import vortex.imwp.models.WarehouseItemID;
import vortex.imwp.repositories.WarehouseItemRepository;

import java.util.*;

@Service
public class WarehouseItemService {
    private final WarehouseItemRepository warehouseItemRepository;
    private final ItemService itemService;
    public WarehouseItemService(WarehouseItemRepository repository, ItemService itemService) {
        this.warehouseItemRepository = repository;
        this.itemService = itemService;
    }

    public Optional<List<WarehouseItemDTO>> findByItemBarcode(Long itemBarcode) {
        Optional<ItemDTO> itemDTO = itemService.getItemByBarcode(itemBarcode);

        if (itemDTO.isPresent()) {
            ItemDTO item = itemDTO.get();
            System.out.println("Item Trial: " + item);

            List<WarehouseItem> tempList = warehouseItemRepository.findAllByItemId(ItemDTOMapper.map(item).getId());
            System.out.println("List: " + tempList);
            List<WarehouseItemDTO> warehouseItemDTOS = new ArrayList<>();

            for (WarehouseItem warehouseItem : tempList) warehouseItemDTOS.add(WarehouseItemDTOMapper.map(warehouseItem));
            return Optional.of(warehouseItemDTOS);
        }
        else{
            return Optional.empty();
        }

    }
    @Transactional
    public void deleteWarehouseItem(WarehouseItemID warehouseItemId) {
        warehouseItemRepository.deleteById(warehouseItemId);
    }
    public void saveWarehouseItem(WarehouseItem wi) {
        warehouseItemRepository.save(wi);
    }

    public List<WarehouseItem> getWarehouseItems(Long warehouseID) {
        return warehouseItemRepository.findAllByWarehouseId(warehouseID);
    }

    public Map<Long, List<Long>> getItemWarehouseIdsMap() {
        List<WarehouseItem> warehouseItems = warehouseItemRepository.findAll();
        Map<Long, List<Long>> result = new HashMap<>();

        for (WarehouseItem wi : warehouseItems) {
            Long itemId = wi.getItem().getId();
            Long warehouseId = wi.getWarehouse().getId();

            result.computeIfAbsent(itemId, k -> new ArrayList<>()).add(warehouseId);
        }

        return result;
    }
}
