package vortex.imwp.Services;

import org.springframework.stereotype.Service;
import vortex.imwp.DTOs.ItemDTO;
import vortex.imwp.DTOs.WarehouseItemDTO;
import vortex.imwp.Mappers.ItemDTOMapper;
import vortex.imwp.Mappers.WarehouseItemDTOMapper;
import vortex.imwp.Models.Item;
import vortex.imwp.Models.Warehouse;
import vortex.imwp.Models.WarehouseItem;
import vortex.imwp.Repositories.WarehouseItemRepository;

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
}
