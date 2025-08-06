package vortex.imwp.DTOs;

import java.sql.Time;

public class SettingsDTO {
    private Long id;
    private EmployeeDTO managerId;
    private Boolean alertWhenStockIsLow;
    private Boolean autoGenerateReport;
    private Time autoGenerateReportTime;
    private Double notifyMinimumCashDiscrepancy;
    private Integer destroyRefundDataAfterNDays;
    private Time cashCountStartTime;
    private Time cashCountEndTime;

    public SettingsDTO() {}
    public SettingsDTO(Long id, EmployeeDTO managerId, Boolean alertWhenStockIsLow, Boolean autoGenerateReport, Time autoGenerateReportTime, Double notifyMinimumCashDiscrepancy, Integer destroyRefundDataAfterNDays, Time cashCountStartTime, Time cashCountEndTime) {
        this.managerId = managerId;
        this.alertWhenStockIsLow = alertWhenStockIsLow;
        this.autoGenerateReport = autoGenerateReport;
        this.autoGenerateReportTime = autoGenerateReportTime;
        this.notifyMinimumCashDiscrepancy = notifyMinimumCashDiscrepancy;
        this.destroyRefundDataAfterNDays = destroyRefundDataAfterNDays;
        this.cashCountStartTime = cashCountStartTime;
        this.cashCountEndTime = cashCountEndTime;
    }

    public Long getId() { return id; }
    public EmployeeDTO getManagerId() { return managerId; }
    public Boolean getAlertWhenStockIsLow() { return alertWhenStockIsLow; }
    public Boolean getAutoGenerateReport() { return autoGenerateReport; }
    public Time getAutoGenerateReportTime() { return autoGenerateReportTime; }
    public Double getNotifyMinimumCashDiscrepancy() { return notifyMinimumCashDiscrepancy; }
    public Integer getDestroyRefundDataAfterNDays() { return destroyRefundDataAfterNDays; }
    public Time getCashCountStartTime() { return cashCountStartTime; }
    public Time getCashCountEndTime() { return cashCountEndTime; }

    public void setId(Long id) { this.id = id; }
    public void setManagerId(EmployeeDTO managerId) { this.managerId = managerId; }
    public void setAlertWhenStockIsLow(Boolean alertWhenStockIsLow) { this.alertWhenStockIsLow = alertWhenStockIsLow; }
    public void setAutoGenerateReport(Boolean autoGenerateReport) { this.autoGenerateReport = autoGenerateReport; }
    public void setAutoGenerateReportTime(Time autoGenerateReportTime) { this.autoGenerateReportTime = autoGenerateReportTime; }
    public void setNotifyMinimumCashDiscrepancy(Double notifyMinimumCashDiscrepancy) { this.notifyMinimumCashDiscrepancy = notifyMinimumCashDiscrepancy; }
    public void setDestroyRefundDataAfterNDays(Integer destroyRefundDataAfterNDays) { this.destroyRefundDataAfterNDays = destroyRefundDataAfterNDays; }
    public void setCashCountStartTime(Time cashCountStartTime) { this.cashCountStartTime = cashCountStartTime; }
    public void setCashCountEndTime(Time cashCountEndTime) { this.cashCountEndTime = cashCountEndTime; }
}
