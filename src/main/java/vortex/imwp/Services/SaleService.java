package vortex.imwp.Services;

import org.springframework.stereotype.Service;
import vortex.imwp.DTOs.SaleDTO;
import vortex.imwp.Mappers.SaleDTOMapper;
import vortex.imwp.Models.Sale;
import vortex.imwp.Repositories.ItemRepository;
import vortex.imwp.Repositories.SaleRepository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SaleService {
    private final SaleRepository saleRepository;
    public SaleService(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
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
        Iterable<Sale> list = saleRepository.findByTimestampBetweenOrderByTimestampAsc(start, end);
        List<SaleDTO> sales = new ArrayList<>();
        if (list.iterator().hasNext()) {
            for (Sale sale : list) sales.add(SaleDTOMapper.map(sale));
            return Optional.of(sales);
        }
        return Optional.empty();
    }

    public Optional<Sale> getSaleById(Long id) { return saleRepository.findById(id); }

    public Sale addSale(SaleDTO sale) { return saleRepository.save(SaleDTOMapper.map(sale)); }

    public Sale updateSale(Sale sale) { return saleRepository.save(sale); }

    public void deleteSale(Long id) { saleRepository.deleteById(id); }
}
