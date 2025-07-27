package vortex.imwp.DTOs;

import jakarta.persistence.*;
import vortex.imwp.Models.Employee;
import vortex.imwp.Models.SaleItem;

import java.security.Timestamp;
import java.util.HashSet;
import java.util.Set;

public class SaleDTO {

    private Timestamp sale_Time;
    private EmployeeDTO salesman;
    //TODO: Data Transfer of Item Quantity in Sale

    public SaleDTO(){}
    public SaleDTO(Timestamp sale_Time, EmployeeDTO salesman) {
        this.sale_Time = sale_Time;
        this.salesman = salesman;
    }

    public Timestamp getSale_Time() { return sale_Time; }
    public void setSale_Time(Timestamp sale_Time) { this.sale_Time = sale_Time; }
    public EmployeeDTO getSalesman() { return salesman; }
    public void setSalesman(EmployeeDTO salesman) { this.salesman = salesman; }
}
