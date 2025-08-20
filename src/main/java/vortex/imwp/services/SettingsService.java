package vortex.imwp.services;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import vortex.imwp.dtos.SettingsDTO;
import vortex.imwp.models.Employee;
import vortex.imwp.models.Settings;
import vortex.imwp.models.SettingsChangeLog;
import vortex.imwp.repositories.SettingsChangeLogRepository;
import vortex.imwp.repositories.SettingsRepository;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
public class SettingsService {

    private final SettingsRepository settingsRepository;
    private final EmployeeService employeeService;
    private final SettingsChangeLogRepository settingsChangeLogRepository;

    public SettingsService(SettingsRepository settingsRepository, EmployeeService employeeService, SettingsChangeLogRepository settingsChangeLogRepository) {
        this.settingsRepository = settingsRepository;
        this.employeeService = employeeService;
        this.settingsChangeLogRepository = settingsChangeLogRepository;
    }

    public boolean checkSettings(Long settingsId) {
        Optional<Settings> checkSettings = settingsRepository.findById(settingsId);
        return checkSettings.isPresent();
    }

    public Optional<Settings> getSettingsById(Long settingsId){
        return settingsRepository.findById(settingsId);
    }

    public Settings getSettingsByWarehouseId(Long warehouseId){
        return settingsRepository.findByWarehouse_Id(warehouseId);
    }

    public void updateSettings(SettingsDTO settingsDto, Authentication authentication) {

        Optional<Settings> settingsCheck = settingsRepository.findById(settingsDto.getId());

        if (settingsCheck.isPresent()) {
            Settings settings = settingsCheck.get();
            StringBuilder sb = new StringBuilder();

            // --- AlertWhenStockIsLow ---
            if (!Objects.equals(settings.getAlertWhenStockIsLow(), settingsDto.getAlertWhenStockIsLow())) {
                sb.append("AlertWhenStockIsLow: ")
                        .append(String.valueOf(settings.getAlertWhenStockIsLow()))
                        .append(" - ")
                        .append(String.valueOf(settingsDto.getAlertWhenStockIsLow()))
                        .append("; ");
                settings.setAlertWhenStockIsLow(settingsDto.getAlertWhenStockIsLow());
            }

            // --- AutoGenerateReport ---
            if (!Objects.equals(settings.getAutoGenerateReport(), settingsDto.getAutoGenerateReport())) {
                sb.append("AutoGenerateReport: ")
                        .append(String.valueOf(settings.getAutoGenerateReport()))
                        .append(" - ")
                        .append(String.valueOf(settingsDto.getAutoGenerateReport()))
                        .append("; ");
                settings.setAutoGenerateReport(settingsDto.getAutoGenerateReport());
            }

            // --- AutoGenerateReportTime ---
            Time newAutoTime = Boolean.TRUE.equals(settingsDto.getAutoGenerateReport())
                    ? settingsDto.getAutoGenerateReportTime()
                    : null;

            if (!Objects.equals(settings.getAutoGenerateReportTime(), newAutoTime)) {
                sb.append("AutoGenerateReportTime: ")
                        .append(settings.getAutoGenerateReportTime() == null ? "null" : settings.getAutoGenerateReportTime().toString())
                        .append(" - ")
                        .append(newAutoTime == null ? "null" : newAutoTime.toString())
                        .append("; ");
                settings.setAutoGenerateReportTime(newAutoTime);
            }

            // --- NotifyMinimumCashDiscrepancy (Double) ---
            if (!Objects.equals(settings.getNotifyMinimumCashDiscrepancy(), settingsDto.getNotifyMinimumCashDiscrepancy())) {
                sb.append("NotifyMinimumCashDiscrepancy: ")
                        .append(String.valueOf(settings.getNotifyMinimumCashDiscrepancy()))
                        .append(" - ")
                        .append(String.valueOf(settingsDto.getNotifyMinimumCashDiscrepancy()))
                        .append("; ");
                settings.setNotifyMinimumCashDiscrepancy(settingsDto.getNotifyMinimumCashDiscrepancy());
            }

            // --- DestroyRefundDataAfterNDays (Integer) ---
            if (!Objects.equals(settings.getDestroyRefundDataAfterNDays(), settingsDto.getDestroyRefundDataAfterNDays())) {
                sb.append("DestroyRefundDataAfterNDays: ")
                        .append(String.valueOf(settings.getDestroyRefundDataAfterNDays()))
                        .append(" - ")
                        .append(String.valueOf(settingsDto.getDestroyRefundDataAfterNDays()))
                        .append("; ");
                settings.setDestroyRefundDataAfterNDays(settingsDto.getDestroyRefundDataAfterNDays());
            }

            // --- CashCountStartTime (Time) ---
            if (!Objects.equals(settings.getCashCountStartTime(), settingsDto.getCashCountStartTime())) {
                sb.append("CashCountStartTime: ")
                        .append(settings.getCashCountStartTime() == null ? "null" : settings.getCashCountStartTime().toString())
                        .append(" - ")
                        .append(settingsDto.getCashCountStartTime() == null ? "null" : settingsDto.getCashCountStartTime().toString())
                        .append("; ");
                settings.setCashCountStartTime(settingsDto.getCashCountStartTime());
            }

            // --- CashCountEndTime (Time) ---
            if (!Objects.equals(settings.getCashCountEndTime(), settingsDto.getCashCountEndTime())) {
                sb.append("CashCountEndTime: ")
                        .append(settings.getCashCountEndTime() == null ? "null" : settings.getCashCountEndTime().toString())
                        .append(" - ")
                        .append(settingsDto.getCashCountEndTime() == null ? "null" : settingsDto.getCashCountEndTime().toString())
                        .append("; ");
                settings.setCashCountEndTime(settingsDto.getCashCountEndTime());
            }

            // --- AutoGenerateInventoryReportTime (Time) ---
            if (!Objects.equals(settings.getAutoGenerateInventoryReportTime(), settingsDto.getAutoGenerateInventoryReportTime())) {
                sb.append("AutoGenerateInventoryReportTime: ")
                        .append(settings.getAutoGenerateInventoryReportTime() == null ? "null" : settings.getAutoGenerateInventoryReportTime().toString())
                        .append(" - ")
                        .append(settingsDto.getAutoGenerateInventoryReportTime() == null ? "null" : settingsDto.getAutoGenerateInventoryReportTime().toString())
                        .append("; ");
                settings.setAutoGenerateInventoryReportTime(settingsDto.getAutoGenerateInventoryReportTime());
            }

            Employee admin = employeeService.getEmployeeByAuthentication(authentication);

            SettingsChangeLog changeLog = new SettingsChangeLog(settings.getId(), settings.getWarehouse().getId(), admin.getId(), sb.toString());

            settingsRepository.save(settings);
            settingsChangeLogRepository.save(changeLog);
            System.out.println("[ " + LocalDateTime.now() + " ]  username " + admin.getUsername() + " Settings Changed: " + sb.toString());
        }

    }




}
