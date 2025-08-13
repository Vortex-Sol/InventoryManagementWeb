package vortex.imwp.services;

import org.springframework.stereotype.Service;
import vortex.imwp.dtos.ItemDTO;
import vortex.imwp.dtos.WarehouseItemDTO;
import vortex.imwp.mappers.ItemDTOMapper;
import vortex.imwp.mappers.WarehouseItemDTOMapper;
import vortex.imwp.models.WarehouseItem;
import vortex.imwp.repositories.WarehouseItemRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public void saveWarehouseItem(WarehouseItem wi) {
        warehouseItemRepository.save(wi);
    }

    public List<WarehouseItem> getWarehouseItems(Long warehouseID) {
        return warehouseItemRepository.findAllByWarehouseId(warehouseID);
    }
}
