package vortex.imwp.dtos;

import java.util.Objects;

public class SaleItemIdDTO {
    private Long saleId;
    private Long itemId;

    public SaleItemIdDTO(){}
    public SaleItemIdDTO(Long saleId, Long itemId) {
        this.saleId = saleId;
        this.itemId = itemId;
    }

    public Long getSaleId() { return saleId; }
    public Long getItemId() { return itemId; }

    public void setSaleId(Long saleId) { this.saleId = saleId; }
    public void setItemId(Long itemId) { this.itemId = itemId; }

    @Override
    public int hashCode() {
        return Objects.hash(saleId, itemId);
    }
}
