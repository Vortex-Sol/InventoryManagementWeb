package vortex.imwp.models;

import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Sale_Time", nullable = false)
    private Timestamp saleTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "Salesman_ID", nullable = false)
    private Employee salesman;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SaleItem> saleItems = new HashSet<>();

    public Sale(){}
    public Sale(Timestamp saleTime, Employee salesman) {
        this.saleTime = saleTime;
        this.salesman = salesman;
    }

    public Long getId() { return id; }
    public Timestamp getSaleTime() { return saleTime; }
    public Employee getSalesman() { return salesman; }
    public Set<SaleItem> getSaleItems() { return saleItems; }
    public void setEmployee(Employee employee) { salesman = employee; }
    public void setSaleTime(Timestamp sale_Time) { this.saleTime = sale_Time; }
}
