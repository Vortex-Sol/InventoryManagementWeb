package vortex.imwp.models;

import jakarta.persistence.*;
import vortex.imwp.dtos.SettingsDTO;
import vortex.imwp.mappers.SettingsDTOMapper;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Settings")
public class Settings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @Column(name = "Alert_When_Stock_Is_Low", nullable = false)
    private Boolean alertWhenStockIsLow;

    @Column(name = "Auto_Generate_Report", nullable = false)
    private Boolean autoGenerateReport;

    @Column(name = "Auto_Generate_Report_Time")
    private Time autoGenerateReportTime;

    @Column(name = "Notify_Minimum_Cash_Discrepancy", nullable = false)
    private Double notifyMinimumCashDiscrepancy;

    @Column(name = "Destroy_Refund_Data_After_N_Days", nullable = false)
    private Integer destroyRefundDataAfterNDays;

    @Column(name = "Cash_Count_Start_Time", nullable = false)
    private Time cashCountStartTime;

    @Column(name = "Cash_Count_End_Time", nullable = false)
    private Time cashCountEndTime;

    @Column(name = "Auto_Generate_Inventory_Report_Time", nullable = false)
    private Time autoGenerateInventoryReportTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "Tax_Rate_id", nullable = false)
    private TaxRate taxRate;

    public Settings() {}
    public Settings(Warehouse warehouse, Boolean alertWhenStockIsLow, Boolean autoGenerateReport, Time autoGenerateReportTime, Double notifyMinimumCashDiscrepancy, Integer destroyRefundDataAfterNDays, Time cashCountStartTime, Time cashCountEndTime, Time autoGenerateInventoryReportTime) {
        this.warehouse = warehouse;
        this.alertWhenStockIsLow = alertWhenStockIsLow;
        this.autoGenerateReport = autoGenerateReport;
        this.autoGenerateReportTime = autoGenerateReportTime;
        this.notifyMinimumCashDiscrepancy = notifyMinimumCashDiscrepancy;
        this.destroyRefundDataAfterNDays = destroyRefundDataAfterNDays;
        this.cashCountStartTime = cashCountStartTime;
        this.cashCountEndTime = cashCountEndTime;
        this.autoGenerateInventoryReportTime = autoGenerateInventoryReportTime;
    }

    public Settings(Warehouse warehouse, Boolean alertWhenStockIsLow, Boolean autoGenerateReport, Time autoGenerateReportTime, Double notifyMinimumCashDiscrepancy, Integer destroyRefundDataAfterNDays, Time cashCountStartTime, Time cashCountEndTime, Time autoGenerateInventoryReportTime, TaxRate taxRate) {
        this.warehouse = warehouse;
        this.alertWhenStockIsLow = alertWhenStockIsLow;
        this.autoGenerateReport = autoGenerateReport;
        this.autoGenerateReportTime = autoGenerateReportTime;
        this.notifyMinimumCashDiscrepancy = notifyMinimumCashDiscrepancy;
        this.destroyRefundDataAfterNDays = destroyRefundDataAfterNDays;
        this.cashCountStartTime = cashCountStartTime;
        this.cashCountEndTime = cashCountEndTime;
        this.autoGenerateInventoryReportTime = autoGenerateInventoryReportTime;
        this.taxRate = taxRate;
    }



    public Long getId() { return id; }
    public Warehouse getWarehouse() { return warehouse; }
    public Boolean getAlertWhenStockIsLow() { return alertWhenStockIsLow; }
    public Boolean getAutoGenerateReport() { return autoGenerateReport; }
    public Time getAutoGenerateReportTime() { return autoGenerateReportTime; }
    public Double getNotifyMinimumCashDiscrepancy() { return notifyMinimumCashDiscrepancy; }
    public Integer getDestroyRefundDataAfterNDays() { return destroyRefundDataAfterNDays; }
    public Time getCashCountStartTime() { return cashCountStartTime; }
    public Time getCashCountEndTime() { return cashCountEndTime; }
    public Time getAutoGenerateInventoryReportTime() { return autoGenerateInventoryReportTime; }
    public TaxRate getTaxRate() { return taxRate; }

    public void setId(Long id) { this.id = id; }
    public void setWarehouse(Warehouse warehouse) { this.warehouse = warehouse; }
    public void setAlertWhenStockIsLow(Boolean alertWhenStockIsLow) { this.alertWhenStockIsLow = alertWhenStockIsLow; }
    public void setAutoGenerateReport(Boolean autoGenerateReport) { this.autoGenerateReport = autoGenerateReport; }
    public void setAutoGenerateReportTime(Time autoGenerateReportTime) { this.autoGenerateReportTime = autoGenerateReportTime; }
    public void setNotifyMinimumCashDiscrepancy(Double notifyMinimumCashDiscrepancy) { this.notifyMinimumCashDiscrepancy = notifyMinimumCashDiscrepancy; }
    public void setDestroyRefundDataAfterNDays(Integer destroyRefundDataAfterNDays) { this.destroyRefundDataAfterNDays = destroyRefundDataAfterNDays; }
    public void setCashCountStartTime(Time cashCountStartTime) { this.cashCountStartTime = cashCountStartTime; }
    public void setCashCountEndTime(Time cashCountEndTime) { this.cashCountEndTime = cashCountEndTime; }
    public void setAutoGenerateInventoryReportTime(Time autoGenerateInventoryReportTime) { this.autoGenerateInventoryReportTime = autoGenerateInventoryReportTime; }
    public void setTaxRate(TaxRate taxRate) { this.taxRate = taxRate; }


    @Override
    public String toString() {
        return "Settings{" +
                "id=" + id +
                ", warehouseId=" + warehouse.getId() +
                ", alertWhenStockIsLow=" + alertWhenStockIsLow +
                ", autoGenerateReport=" + autoGenerateReport +
                ", autoGenerateReportTime=" + autoGenerateReportTime +
                ", notifyMinimumCashDiscrepancy=" + notifyMinimumCashDiscrepancy +
                ", destroyRefundDataAfterNDays=" + destroyRefundDataAfterNDays +
                ", cashCountStartTime=" + cashCountStartTime +
                ", cashCountEndTime=" + cashCountEndTime +
                ", autoGenerateInventoryReportTime=" + autoGenerateInventoryReportTime +
                '}';
    }
}
