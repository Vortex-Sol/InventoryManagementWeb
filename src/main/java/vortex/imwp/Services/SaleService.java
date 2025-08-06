package vortex.imwp.Services;

import org.springframework.stereotype.Service;
import vortex.imwp.DTOs.SaleDTO;
import vortex.imwp.Mappers.SaleDTOMapper;
import vortex.imwp.Models.Employee;
import vortex.imwp.Models.Item;
import vortex.imwp.Models.Sale;
import vortex.imwp.Models.SaleItem;
import vortex.imwp.Repositories.EmployeeRepository;
import vortex.imwp.Repositories.ItemRepository;
import vortex.imwp.Repositories.SaleRepository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SaleService {
    private final SaleRepository saleRepository;
    private final ItemRepository itemRepository;
    private final EmployeeRepository employeeRepository;

    public SaleService(SaleRepository saleRepository, ItemRepository itemRepository, EmployeeRepository employeeRepository) {
        this.saleRepository = saleRepository;
        this.itemRepository = itemRepository;
        this.employeeRepository = employeeRepository;
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

    public Sale addItemToSale(Long saleId, Long itemId, int quantity) {
        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(() -> new RuntimeException("Sale not found"));

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        SaleItem saleItem = new SaleItem(sale, item, quantity);
        sale.getSaleItems().add(saleItem);
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
