package vortex.imwp.Models;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Receipt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "Sale_ID")
    private Sale sale;

    @Column(name = "Total_Amount", nullable = false, precision = 10, scale = 3)
    private BigDecimal totalAmount;

    @Column(name = "Created_At", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "Payment_Method", nullable = false, length = 50)
    private String paymentMethod;

    public Receipt() {}
    public Receipt(Sale sale, BigDecimal totalAmount, String paymentMethod) {
        this.sale = sale;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Sale getSale() { return sale; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getPaymentMethod() { return paymentMethod; }

    public void setSale(Sale sale) { this.sale = sale; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
}
