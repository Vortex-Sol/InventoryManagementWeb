package vortex.imwp.dtos;

public class SaleItemDTO {
    private SaleItemIdDTO id = new SaleItemIdDTO();
    private SaleDTO sale;
    private ItemDTO item;
    private int quantity;

    public SaleItemDTO() {}
    public SaleItemDTO(SaleDTO sale, ItemDTO item, int quantity) {
        this.sale = sale;
        this.item = item;
        this.quantity = quantity;
    }
    public SaleItemDTO(SaleItemIdDTO id, SaleDTO sale, ItemDTO item, int quantity) {
        this.id = id;
        this.sale = sale;
        this.item = item;
        this.quantity = quantity;
    }

    public SaleItemIdDTO getId() { return id; }
    public SaleDTO getSale() { return sale; }
    public ItemDTO getItem() { return item; }
    public int getQuantity() { return quantity; }

    public void setId(SaleItemIdDTO id) { this.id = id; }
    public void setSale(SaleDTO sale) { this.sale = sale; }
    public void setItem(ItemDTO item) { this.item = item; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
