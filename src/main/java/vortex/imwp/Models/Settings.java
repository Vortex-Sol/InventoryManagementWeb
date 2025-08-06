package vortex.imwp.Models;

import jakarta.persistence.*;

import java.sql.Time;

@Entity
@Table(name = "Settings")
public class Settings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "Manager_ID", nullable = false)
    private Employee managerId;

    @JoinColumn(name = "Alert_When_Stock_Is_Low", nullable = false)
    private Boolean alertWhenStockIsLow;

    @JoinColumn(name = "Auto_Generate_Report", nullable = false)
    private Boolean autoGenerateReport;

    @JoinColumn(name = "Auto_Generate_Report_Time")
    private Time autoGenerateReportTime;

    @JoinColumn(name = "Notify_Minimum_Cash_Discrepancy", nullable = false)
    private Double notifyMinimumCashDiscrepancy;

    @JoinColumn(name = "Destroy_Refund_Data_After_N_Days", nullable = false)
    private Integer destroyRefundDataAfterNDays;

    @JoinColumn(name = "Cash_Count_Start_Time", nullable = false)
    private Time cashCountStartTime;

    @JoinColumn(name = "Cash_Count_End_Time", nullable = false)
    private Time cashCountEndTime;

    public Settings() {}
    public Settings(Employee managerId, Boolean alertWhenStockIsLow, Boolean autoGenerateReport, Time autoGenerateReportTime, Double notifyMinimumCashDiscrepancy, Integer destroyRefundDataAfterNDays, Time cashCountStartTime, Time cashCountEndTime) {
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
    public Employee getManagerId() { return managerId; }
    public Boolean getAlertWhenStockIsLow() { return alertWhenStockIsLow; }
    public Boolean getAutoGenerateReport() { return autoGenerateReport; }
    public Time getAutoGenerateReportTime() { return autoGenerateReportTime; }
    public Double notifyMinimumCashDiscrepancy() { return notifyMinimumCashDiscrepancy; }
    public Integer getDestroyRefundDataAfterNDays() { return destroyRefundDataAfterNDays; }
    public Time getCashCountStartTime() { return cashCountStartTime; }
    public Time getCashCountEndTime() { return cashCountEndTime; }

    public void setId(Long id) { this.id = id; }
    public void setManagerId(Employee managerId) { this.managerId = managerId; }
    public void setAlertWhenStockIsLow(Boolean alertWhenStockIsLow) { this.alertWhenStockIsLow = alertWhenStockIsLow; }
    public void setAutoGenerateReport(Boolean autoGenerateReport) { this.autoGenerateReport = autoGenerateReport; }
    public void setAutoGenerateReportTime(Time autoGenerateReportTime) { this.autoGenerateReportTime = autoGenerateReportTime; }
    public void setNotifyMinimumCashDiscrepancy(Double notifyMinimumCashDiscrepancy) { this.notifyMinimumCashDiscrepancy = notifyMinimumCashDiscrepancy; }
    public void setDestroyRefundDataAfterNDays(Integer destroyRefundDataAfterNDays) { this.destroyRefundDataAfterNDays = destroyRefundDataAfterNDays; }
    public void setCashCountStartTime(Time cashCountStartTime) { this.cashCountStartTime = cashCountStartTime; }
    public void setCashCountEndTime(Time cashCountEndTime) { this.cashCountEndTime = cashCountEndTime; }
}
