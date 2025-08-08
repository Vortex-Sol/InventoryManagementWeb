package vortex.imwp.Services;

import org.springframework.stereotype.Service;
import vortex.imwp.DTOs.SaleDTO;
import vortex.imwp.Mappers.SaleDTOMapper;
import vortex.imwp.Models.*;
import vortex.imwp.Repositories.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SaleService {
    private final SaleRepository saleRepository;
    private final ItemRepository itemRepository;
    private final EmployeeRepository employeeRepository;
    private final WarehouseItemRepository warehouseItemRepository;

    public SaleService(SaleRepository saleRepository, ItemRepository itemRepository
            , EmployeeRepository employeeRepository, WarehouseItemRepository warehouseItemRepository) {
        this.saleRepository = saleRepository;
        this.itemRepository = itemRepository;
        this.employeeRepository = employeeRepository;
        this.warehouseItemRepository = warehouseItemRepository;
    }
    public Optional<List<SaleDTO>> getAll() {
        Iterable<Sale> list = saleRepository.findAll();
        List<SaleDTO> sales = new ArrayList<>();
        if (list.iterator().hasNext()) {
            for (Sale sale : list) sales.add(SaleDTOMapper.map(sale));
            return Optional.of(sales);
        }
        return Optional.empty();
    }

    //TODO: Fix Needed - Program Does not Compile
    public Optional<List<SaleDTO>> getByPeriod(Timestamp start, Timestamp end) {
        /*Iterable<Sale> list = saleRepository.findByTimestampBetweenOrderByTimestampAsc(start, end);
        List<SaleDTO> sales = new ArrayList<>();
        if (list.iterator().hasNext()) {
            for (Sale sale : list) sales.add(SaleDTOMapper.map(sale));
            return Optional.of(sales);
        }*/
        return Optional.empty();
    }

    public Sale createSale(String username) {
        Employee employee = employeeRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        Sale sale = new Sale();
        sale.setSale_Time(new Timestamp(System.currentTimeMillis()));
        sale.setEmployee(employee);
        return saleRepository.save(sale);
    }

    public Sale addItemToSale(Long saleId, Long warehouseId, Long itemId, int quantity) {
        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(() -> new RuntimeException("Sale not found"));

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        WarehouseItem warehouseItem = warehouseItemRepository.findByWarehouseIdAndItemId(warehouseId, itemId)
                .orElseThrow(() -> new RuntimeException("Item not available in selected warehouse"));

        if (warehouseItem.getQuantityInStock() < quantity) {
            throw new IllegalArgumentException("Insufficient stock in selected warehouse");
        }

        warehouseItem.setQuantityInStock(warehouseItem.getQuantityInStock() - quantity);
        warehouseItemRepository.save(warehouseItem);

        Optional<SaleItem> existing = sale.getSaleItems().stream()
                .filter(si -> si.getItem().getId().equals(itemId))
                .findFirst();

        if (existing.isPresent()) {
            SaleItem saleItem = existing.get();
            saleItem.setQuantity(saleItem.getQuantity() + quantity);
        } else {
            SaleItem saleItem = new SaleItem(sale, item, quantity);
            sale.getSaleItems().add(saleItem);
        }

        return saleRepository.save(sale);
    }


    public Sale getSaleById(Long saleId) {
        return saleRepository.findById(saleId)
                .orElseThrow(() -> new RuntimeException("Sale not found"));
    }

    public Sale addSale(SaleDTO sale) { return saleRepository.save(SaleDTOMapper.map(sale)); }

    public Sale updateSale(Sale sale) { return saleRepository.save(sale); }

    public void deleteSale(Long id) { saleRepository.deleteById(id); }
}
