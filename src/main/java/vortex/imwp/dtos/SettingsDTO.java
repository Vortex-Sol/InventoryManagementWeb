package vortex.imwp.dtos;

import java.sql.Time;

public class SettingsDTO {
    private Long id;
    private WarehouseDTO warehouse;
    private Boolean alertWhenStockIsLow;
    private Boolean autoGenerateReport;
    private Time autoGenerateReportTime;
    private Double notifyMinimumCashDiscrepancy;
    private Integer destroyRefundDataAfterNDays;
    private Time cashCountStartTime;
    private Time cashCountEndTime;
    private Time autoGenerateInventoryReportTime;

    public SettingsDTO() {}
    public SettingsDTO(Long id, WarehouseDTO warehouse, Boolean alertWhenStockIsLow, Boolean autoGenerateReport, Time autoGenerateReportTime, Double notifyMinimumCashDiscrepancy, Integer destroyRefundDataAfterNDays, Time cashCountStartTime, Time cashCountEndTime, Time autoGenerateInventoryReportTime) {
        this.id = id;
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

    public Long getId() { return id; }
    public WarehouseDTO getWarehouse() {return warehouse;}
    public Boolean getAlertWhenStockIsLow() { return alertWhenStockIsLow; }
    public Boolean getAutoGenerateReport() { return autoGenerateReport; }
    public Time getAutoGenerateReportTime() { return autoGenerateReportTime; }
    public Double getNotifyMinimumCashDiscrepancy() { return notifyMinimumCashDiscrepancy; }
    public Integer getDestroyRefundDataAfterNDays() { return destroyRefundDataAfterNDays; }
    public Time getCashCountStartTime() { return cashCountStartTime; }
    public Time getCashCountEndTime() { return cashCountEndTime; }
    public Time getAutoGenerateInventoryReportTime() { return autoGenerateInventoryReportTime; }

    public void setId(Long id) { this.id = id; }
    public void setWarehouse(WarehouseDTO warehouse) { this.warehouse = warehouse; }
    public void setAlertWhenStockIsLow(Boolean alertWhenStockIsLow) { this.alertWhenStockIsLow = alertWhenStockIsLow; }
    public void setAutoGenerateReport(Boolean autoGenerateReport) { this.autoGenerateReport = autoGenerateReport; }
    public void setAutoGenerateReportTime(Time autoGenerateReportTime) { this.autoGenerateReportTime = autoGenerateReportTime; }
    public void setNotifyMinimumCashDiscrepancy(Double notifyMinimumCashDiscrepancy) { this.notifyMinimumCashDiscrepancy = notifyMinimumCashDiscrepancy; }
    public void setDestroyRefundDataAfterNDays(Integer destroyRefundDataAfterNDays) { this.destroyRefundDataAfterNDays = destroyRefundDataAfterNDays; }
    public void setCashCountStartTime(Time cashCountStartTime) { this.cashCountStartTime = cashCountStartTime; }
    public void setCashCountEndTime(Time cashCountEndTime) { this.cashCountEndTime = cashCountEndTime; }
    public void setAutoGenerateInventoryReportTime(Time autoGenerateInventoryReportTime) { this.autoGenerateInventoryReportTime = autoGenerateInventoryReportTime; }

    @Override
    public String toString() {
        return "SettingsDTO{" +
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
