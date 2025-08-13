package vortex.imwp.dtos;

public class ItemDTO {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Long barcode;
    private CategoryDTO category;

    public ItemDTO() {}
    public ItemDTO(String name, String description, Double price, Long barcode, CategoryDTO category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.barcode = barcode;
        this.category = category;
    }
    public ItemDTO(Long id, String name, String description, Double price, Long barcode, CategoryDTO category) {
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
    public CategoryDTO getCategory() { return category; }

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setPrice(Double price) { this.price = price; }
    public void setBarcode(Long barcode) { this.barcode = barcode; }
    public void setCategory(CategoryDTO category) { this.category = category; }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", barcode=" + barcode +
                '}';
    }
}
