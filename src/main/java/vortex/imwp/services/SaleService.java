package vortex.imwp.services;

import org.springframework.stereotype.Service;
import vortex.imwp.dtos.ItemDTO;
import vortex.imwp.dtos.SaleDTO;
import vortex.imwp.mappers.ItemDTOMapper;
import vortex.imwp.mappers.SaleDTOMapper;
import vortex.imwp.models.*;
import vortex.imwp.repositories.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

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

    public Optional<List<SaleDTO>> getByPeriod(Timestamp start, Timestamp end) {
        Iterable<Sale> list = saleRepository.findSalesBetweenTimestamps(start, end);
        List<SaleDTO> sales = new ArrayList<>();
        if (list.iterator().hasNext()) {
            for (Sale sale : list) sales.add(SaleDTOMapper.map(sale));
            return Optional.of(sales);
        }
        return Optional.empty();
    }

    public List<Sale> getByEmployee(Employee employee) {
        return saleRepository.findSalesBySalesman(employee);
    }

    public List<Sale> getByEmployeeAndDate(Employee employee, LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();
        return saleRepository.findSalesBySalesmanAndSaleTimeGreaterThanEqualAndSaleTimeLessThan(
                employee, Timestamp.valueOf(start), Timestamp.valueOf(end));
    }

    public List<Sale> getByEmployeeAndPeriod(Employee employee, Timestamp start, Timestamp end) {
        return saleRepository.findSalesBySalesmanAndSaleTimeGreaterThanEqualAndSaleTimeLessThan(
                employee, start, end);
    }

    public List<Sale> getByWarehouseIdAndDate(Long warehouseID, LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();
        return saleRepository.findSalesBySalesman_WarehouseIDAndSaleTimeGreaterThanEqualAndSaleTimeLessThan(
                warehouseID, Timestamp.valueOf(start), Timestamp.valueOf(end));
    }

    public List<Sale> getByWarehouseIdAndPeriod(Long warehouseID, Timestamp start, Timestamp end) {
        return saleRepository.findSalesBySalesman_WarehouseIDAndSaleTimeGreaterThanEqualAndSaleTimeLessThan(
                warehouseID, start, end);
    }

    public Sale createSale(String username) {
        Employee employee = employeeRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        Sale sale = new Sale();
        sale.setSaleTime(new Timestamp(System.currentTimeMillis()));
        sale.setEmployee(employee);
        return saleRepository.save(sale);
    }

    public Map<ItemDTO, Integer> getItemsWithQuantity(Long saleId) {
        Map<ItemDTO, Integer> items = new HashMap<>();
        Optional<Sale> saleCheck = saleRepository.findById(saleId);
        if (saleCheck.isPresent()) {
            Sale sale = saleCheck.get();
            Set<SaleItem> saleItems =sale.getSaleItems();
            for (SaleItem saleItem : saleItems) {
                items.put(ItemDTOMapper.map(saleItem.getItem()), saleItem.getQuantity());
            }
            return items;
        }
        return items;
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

    /*public Sale removeItemToSale(Long saleId, Long warehouseId, Long itemId, int quantity) {
        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(() -> new RuntimeException("Sale not found"));

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        WarehouseItem warehouseItem = warehouseItemRepository.findByWarehouseIdAndItemId(warehouseId, itemId)
                .orElseThrow(() -> new RuntimeException("Item not available in selected warehouse"));

        warehouseItem.setQuantityInStock(warehouseItem.getQuantityInStock() + quantity);
        warehouseItemRepository.save(warehouseItem);

        Optional<SaleItem> existing = sale.getSaleItems().stream()
                .filter(si -> si.getItem().getId().equals(itemId))
                .findFirst();
    }*/

    public Sale getSaleById(Long saleId) {
        return saleRepository.findById(saleId)
                .orElseThrow(() -> new RuntimeException("Sale not found"));
    }

    public Sale addSale(SaleDTO sale) { return saleRepository.save(SaleDTOMapper.map(sale)); }

    public Sale updateSale(Sale sale) { return saleRepository.save(sale); }

    public void deleteSale(Long id) { saleRepository.deleteById(id); }
}
