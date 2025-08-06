package vortex.imwp.DTOs;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ReceiptDTO {
    private Long id;
    private SaleDTO sale;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
    private String paymentMethod;
    private BigDecimal amountReceived;
    private BigDecimal changeGiven;

    public ReceiptDTO() {}
    public ReceiptDTO(SaleDTO sale, BigDecimal totalAmount, String paymentMethod) {
        this.sale = sale;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
        this.createdAt = LocalDateTime.now();
    }

    public ReceiptDTO(Long id, SaleDTO sale, BigDecimal totalAmount, LocalDateTime createdAt, String paymentMethod, BigDecimal amountReceived, BigDecimal changeGiven) {
        this.id = id;
        this.sale = sale;
        this.totalAmount = totalAmount;
        this.createdAt = createdAt;
        this.paymentMethod = paymentMethod;
        this.amountReceived = amountReceived;
        this.changeGiven = changeGiven;
    }

    public Long getId() { return id; }
    public SaleDTO getSale() { return sale; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getPaymentMethod() { return paymentMethod; }
    public BigDecimal getAmountReceived() { return amountReceived; }
    public BigDecimal getChangeGiven() { return changeGiven; }

    public void setSale(SaleDTO sale) { this.sale = sale; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public void setAmountReceived(BigDecimal amountReceived) { this.amountReceived = amountReceived; }
    public void setChangeGiven(BigDecimal changeGiven) { this.changeGiven = changeGiven; }
}
