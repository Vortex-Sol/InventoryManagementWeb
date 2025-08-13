package vortex.imwp.mappers;

import vortex.imwp.dtos.SaleDTO;
import vortex.imwp.models.Sale;

public class SaleDTOMapper {
    public static SaleDTO map(Sale sale){
        return new SaleDTO(
                sale.getSaleTime(),
                EmployeeDTOMapper.map(sale.getSalesman())
        );
    }

    public static Sale map(SaleDTO sale){
        return new Sale(
                sale.getSale_Time(),
                EmployeeDTOMapper.map(sale.getSalesman())
        );
    }
}
