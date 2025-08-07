package vortex.imwp.Models;

import jakarta.persistence.*;

@Entity
@Table(name = "Item")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private Double price;
    @Column(nullable = false)
    private Long barcode;

    @ManyToOne
    @JoinColumn(name = "Category_ID")
    private Category category;

    public Item() {}
    public Item(String name, String description, Double price, Long barcode) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.barcode = barcode;
    }

    public Item(Long id, String name, String description, Double price, Long barcode) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.barcode = barcode;
    }

    public Item(Long id, String name, String description, Double price, Long barcode, Category category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.barcode = barcode;
        this.category = category;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public Double getPrice() { return price; }
    public Long getBarcode() { return barcode; }
    public Category getCategory() { return category; }

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setPrice(Double price) { this.price = price; }
    public void setBarcode(Long barcode) { this.barcode = barcode; }
    public void setCategory(Category category) { this.category = category; }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", barcode=" + barcode +
                ", category=" + category +
                '}';
    }
}
