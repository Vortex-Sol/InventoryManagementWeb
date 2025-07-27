package vortex.imwp.Models;

import jakarta.persistence.*;

@Entity
public class SaleItem {
    @EmbeddedId
    private SaleItemID id = new SaleItemID();

    @ManyToOne
    @MapsId("saleId")
    @JoinColumn(name = "Sale_ID")
    private Sale sale;

    @ManyToOne
    @MapsId("itemId")
    @JoinColumn(name = "Item_ID")
    private Item item;

    private int quantity;

    public SaleItem() {}
    public SaleItem(Sale sale, Item item, int quantity) {
        this.sale = sale;
        this.item = item;
        this.quantity = quantity;
    }

    public SaleItemID getId() { return id; }
    public void setId(SaleItemID id) { this.id = id; }
    public Sale getSale() { return sale; }
    public void setSale(Sale sale) { this.sale = sale; }
    public Item getItem() { return item; }
    public void setItem(Item item) { this.item = item; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
