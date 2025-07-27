package vortex.imwp.Models;

import java.io.Serializable;
import java.util.Objects;

public class SaleItemID implements Serializable {
    private Long saleId;
    private Long itemId;

    public SaleItemID(){}
    public SaleItemID(Long saleId, Long itemId) {
        this.saleId = saleId;
        this.itemId = itemId;
    }

    public Long getSaleId() { return saleId; }
    public void setSaleId(Long saleId) { this.saleId = saleId; }
    public Long getItemId() { return itemId; }
    public void setItemId(Long itemId) { this.itemId = itemId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SaleItemID)) return false;
        SaleItemID that = (SaleItemID) o;
        return Objects.equals(saleId, that.saleId) &&
                Objects.equals(itemId, that.itemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(saleId, itemId);
    }
}
