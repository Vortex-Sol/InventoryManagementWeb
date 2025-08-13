package vortex.imwp.dtos;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SaleDTO {

    private Timestamp sale_Time;
    private EmployeeDTO salesman;
    //TODO: Data Transfer of Item Quantity in Sale
    private Map<ItemDTO, Integer> items;

    public SaleDTO(){}
    public SaleDTO(Timestamp sale_Time, EmployeeDTO salesman) {
        this.sale_Time = sale_Time;
        this.salesman = salesman;
    }

    public SaleDTO(Timestamp sale_Time, EmployeeDTO salesman, List<ItemDTO> items) {
        this.sale_Time = sale_Time;
        this.salesman = salesman;
        this.items = new HashMap<>();
    }

    public Timestamp getSale_Time() { return sale_Time; }
    public void setSale_Time(Timestamp sale_Time) { this.sale_Time = sale_Time; }
    public EmployeeDTO getSalesman() { return salesman; }
    public void setSalesman(EmployeeDTO salesman) { this.salesman = salesman; }

    public Map<ItemDTO, Integer> getItems() { return items; }
    public void setItems(Map<ItemDTO, Integer> items) { this.items = items; }
    public void addItem(ItemDTO item, int quantity) { this.items.put(item, quantity); }
}
