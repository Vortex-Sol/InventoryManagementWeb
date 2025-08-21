package vortex.imwp.services;

import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import vortex.imwp.dtos.SettingsDTO;
import vortex.imwp.dtos.TaxRateDTO;
import vortex.imwp.mappers.SettingsDTOMapper;
import vortex.imwp.mappers.TaxRateDTOMapper;
import vortex.imwp.models.*;
import vortex.imwp.repositories.SettingsChangeAuditRepository;
import vortex.imwp.repositories.SettingsRepository;
import vortex.imwp.repositories.TaxRateRepository;
import vortex.imwp.repositories.WarehouseRepository;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
public class SettingsService {

    private final SettingsRepository settingsRepository;
    private final EmployeeService employeeService;
    private final SettingsChangeAuditRepository settingsChangeAuditRepository;
    private final WarehouseRepository warehouseRepository;
    private final TaxRateRepository taxRateRepository;
    private final WarehouseService warehouseService;

    public SettingsService(SettingsRepository settingsRepository, EmployeeService employeeService, SettingsChangeAuditRepository settingsChangeAuditRepository, WarehouseRepository warehouseRepository, TaxRateRepository taxRateRepository, WarehouseService warehouseService) {
        this.settingsRepository = settingsRepository;
        this.employeeService = employeeService;
        this.settingsChangeAuditRepository = settingsChangeAuditRepository;
        this.warehouseRepository = warehouseRepository;
        this.taxRateRepository = taxRateRepository;
        this.warehouseService = warehouseService;
    }
    @Transactional
    public void deleteById(long id) {
        settingsRepository.deleteById(id);
    }

    public boolean checkSettings(Long settingsId) {
        Optional<Settings> checkSettings = settingsRepository.findById(settingsId);
        return checkSettings.isPresent();
    }
    public Settings createDefaultSettingsForWarehouse(Warehouse warehouse, TaxRate taxRate) {
        //todo that gets the max id value and ++1
        warehouse.setId(warehouseService.generateId());
        taxRateRepository.save(taxRate);
        warehouseRepository.save(warehouse);
        Settings setting =  new Settings(warehouse,false, true, new Time(00,00,00), 500.00, 14, new Time(06,00,00), new Time(23,00,00), new Time(23,00,00), taxRate);
        System.out.println("[TESTING] 3 :" + setting);
        settingsRepository.save(setting);
        return setting;
    }
    public Optional<Settings> getSettingsById(Long settingsId){
        return settingsRepository.findById(settingsId);
    }

    public Settings getSettingsByWarehouseId(Long warehouseId){
        return settingsRepository.findByWarehouse_Id(warehouseId);
    }
    public SettingsDTO getSettingsByWarehouseDTOId(Long warehouseId) {
        Settings settings = settingsRepository.findByWarehouse_Id(warehouseId);
        return SettingsDTOMapper.map(settings);
    }
    public TaxRateDTO getTaxRateDTOById(Long id) {
        TaxRate TaxRate  = settingsRepository.findById(id).get().getTaxRate();
        return TaxRateDTOMapper.map(TaxRate);
    }

    public void updateTaxRate(Long id, TaxRate taxRate) {
        settingsRepository.findById(id).get().setTaxRate(taxRate);
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

            SettingsChangeAudit changeLog = new SettingsChangeAudit(settings.getId(), settings.getWarehouse().getId(), admin.getId(), sb.toString());

            settingsRepository.save(settings);
            settingsChangeAuditRepository.save(changeLog);
            System.out.println("[ " + LocalDateTime.now() + " ]  username " + admin.getUsername() + " Settings Changed: " + sb.toString());
        }

    }




}
