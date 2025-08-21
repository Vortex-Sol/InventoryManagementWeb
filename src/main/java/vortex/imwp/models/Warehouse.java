package vortex.imwp.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Warehouse")
public class Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="Phone", nullable = false)
    private String phone;
    @Column(name="Email", nullable = false)
    private String email;
    @Column(name="Address", nullable = false)
    private String address;

    @OneToOne(mappedBy = "warehouse", cascade = CascadeType.ALL, orphanRemoval = true)
    private Settings settings;

    @Enumerated(EnumType.STRING)
    @Column(name = "Country", nullable = false)
    private Country.Name country;

    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WarehouseItem> warehouseItems = new ArrayList<>();

    public Warehouse() {}
    public Warehouse(String phone, String email, String address) {
        this.phone = phone;
        this.email = email;
        this.address = address;
    }

    public Warehouse(String phone, String email, String address, Country.Name country) {
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.country = country;
    }

    public Warehouse(String phone, String email, String address, Settings settings, Country.Name country) {
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.settings = settings;
        this.country = country;
    }

    public Long getId() { return id; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getAddress() { return address; }
    public Settings getSettings() { return settings; }
    public Country.Name getCountry() { return country; }
    public List<WarehouseItem> getWarehouseItems() { return warehouseItems; }

    public void setId(Long id) { this.id = id; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setEmail(String email) { this.email = email; }
    public void setSettings(Settings settings) { this.settings = settings; }
    public void setWarehouseItems(List<WarehouseItem> warehouseItems) { this.warehouseItems = warehouseItems; }
    public void setAddress(String address) { this.address = address; }
    public void setCountry(Country.Name country) { this.country = country; }
}
