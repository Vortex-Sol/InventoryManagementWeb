package vortex.imwp.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import vortex.imwp.dtos.ReportDTO;
import vortex.imwp.mappers.ReportDTOMapper;
import vortex.imwp.models.*;
import vortex.imwp.repositories.ReportRepository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ReportService {
    private final ReportRepository reportRepository;
    private final EmployeeService employeeService;
    private final LoginAuditService loginAuditService;
    private final SaleService saleService;
    private final WarehouseItemService warehouseItemService;
    private final ReceiptService receiptService;
    private final LogoutAuditService logoutAuditService;
    private final SettingsChangeAuditService settingsChangeAuditService;

    public ReportService(ReportRepository reportRepository, EmployeeService employeeService, LoginAuditService loginAuditService, SaleService saleService, WarehouseItemService warehouseItemService, ReceiptService receiptService, LogoutAuditService logoutAuditService, SettingsChangeAuditService settingsChangeAuditService) {
        this.reportRepository = reportRepository;
        this.employeeService = employeeService;
        this.loginAuditService = loginAuditService;
        this.saleService = saleService;
        this.warehouseItemService = warehouseItemService;
        this.receiptService = receiptService;
        this.logoutAuditService = logoutAuditService;
        this.settingsChangeAuditService = settingsChangeAuditService;
    }

    public Optional<List<ReportDTO>> getAll() {
        Iterable<Report> list = reportRepository.findAll();
        List<ReportDTO> reports = new ArrayList<>();
        if (list.iterator().hasNext()) {
            for (Report report : list) reports.add(ReportDTOMapper.map(report));
            return Optional.of(reports);
        }
        return Optional.empty();
    }

    public Optional<Report> getReportById(Long id) { return reportRepository.findById(id); }

    public Report addReport(ReportDTO report) { return reportRepository.save(ReportDTOMapper.map(report)); }

    public Report updateReport(Report report) { return reportRepository.save(report); }

    public void deleteReport(Long id) { reportRepository.deleteById(id); }

    public JSONObject generateTodayGeneralReport() { return new JSONObject(); }

    public JSONObject generateTodayEmployeeReport(Authentication auth) throws JsonProcessingException {

        Employee manager = employeeService.getEmployeeByAuthentication(auth);

        String timestamp = LocalDateTime.now().toString();
        LocalDate today = LocalDate.now();

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("employeeIdCreated", manager.getId());;
        body.put("createdAtWarehouseID", manager.getWarehouseID());
        body.put("timestamp", timestamp);

        List<Object> employeesData = new ArrayList<>();
        List<Employee> employees = employeeService.getAllActiveTodayEmployeesFromWarehouse(manager.getWarehouseID(), today);
        for (Employee employee : employees) {
            Map<String, Object> employeeData = new LinkedHashMap<>();
            employeeData.put("employeeId", employee.getId());
            employeeData.put("username", employee.getUsername());
            List<String> jobNames = employee.getJobs().stream().map(Job::getName).toList();
            employeeData.put("jobs", jobNames);

            List<Map<String, Object>> loginAudits = loginAuditService.getLoginAuditsByEmployeeAndDate(employee, today)
                    .stream().map(audit -> Map.<String, Object>of(
                            "loginTime", audit.getLoginTime(),
                            "ipAddress", audit.getIpAddress(),
                            "successFailure", audit.getSuccessFailure()
                    )).toList();
            employeeData.put("loginAudits", loginAudits);

            List<Map<String, Object>> logoutAudits = logoutAuditService.getLogoutAuditsByEmployeeAndDate(employee, today)
                    .stream().map(audit -> Map.<String, Object>of(
                            "logoutTime", audit.getLogoutTime(),
                            "ipAddress", audit.getIpAddress(),
                            "reason", audit.getLogoutReason()
                    )).toList();
            employeeData.put("logoutAudits", logoutAudits);

            if (jobNames.contains("ADMIN")){
                List<Object> settingsChangedData = new ArrayList<>();
                List<SettingsChangeAudit> settingsChangeAudits = settingsChangeAuditService.getSettingsChangeLogsByEmployeeAndDate(employee, today);
                for (SettingsChangeAudit settingsChangeAudit : settingsChangeAudits) {
                    Map<String, Object> settingChangeData = new LinkedHashMap<>();
                    settingChangeData.put("settingsChangeLogId", settingsChangeAudit.getId());
                    settingChangeData.put("changedAt", settingsChangeAudit.getChangedAt());
                    settingChangeData.put("settingId", settingsChangeAudit.getSettingId());
                    settingChangeData.put("settingsChanged", settingsChangeAudit.getSettingsChanged());

                    settingsChangedData.add(settingChangeData);
                }
                if (!settingsChangedData.isEmpty()) {
                    employeeData.put("settingsChangeLogs", settingsChangeAudits);
                }

            }

            if (jobNames.contains("SALESMAN")){
                List<Object> receiptsData = new ArrayList<>();
                List<Receipt> receipts = receiptService.getByEmployeeAndDate(employee, today);
                ObjectMapper mapper = new ObjectMapper();
                for (Receipt receipt : receipts) {
                    String receiptString = receiptService.generateReceiptJson(receipt);
                    Map<String, Object> parsed = mapper.readValue(receiptString, new TypeReference<Map<String,Object>>() {});
                    receiptsData.add(parsed);
                }
                if (!receipts.isEmpty()) {
                    employeeData.put("receiptsData", receiptsData);
                }
            }



            employeesData.add(employeeData);
        }
        body.put("employeesData", employeesData);

        JSONObject reportData = new JSONObject(body);

        Report report = new Report(ReportType.Type.EMPLOYEES, manager.getId(), manager.getWarehouseID(), reportData);
        report = reportRepository.save(report);
        body.put("reportId", report.getId());
        reportData = new JSONObject(body);
        report.setData(reportData);
        reportRepository.save(report);

        return reportData;

    }

    public JSONObject generateTodayInventoryReport(Authentication auth) {

        Employee manager = employeeService.getEmployeeByAuthentication(auth);

        String timestamp = LocalDateTime.now().toString();

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("employeeIdCreated", manager.getId());;
        body.put("createdAtWarehouseID", manager.getWarehouseID());
        body.put("timestamp", timestamp);

        List<Object> itemsData = new ArrayList<>();
        List<WarehouseItem> warehouseItems = warehouseItemService.getWarehouseItems(manager.getWarehouseID());
        for (WarehouseItem warehouseItem : warehouseItems) {
            Map<String, Object> itemData = new LinkedHashMap<>();
            itemData.put("itemID", warehouseItem.getItem().getId());
            itemData.put("itemName", warehouseItem.getItem().getName());
            itemData.put("price", warehouseItem.getItem().getPrice());
            itemData.put("barcode", warehouseItem.getItem().getBarcode());
            itemData.put("category", warehouseItem.getItem().getCategory().getName());
            itemData.put("sku", warehouseItem.getSku());
            itemData.put("quantityInStock", warehouseItem.getQuantityInStock());

            itemsData.add(itemData);
        }
        body.put("itemsData", itemsData);

        JSONObject reportData = new JSONObject(body);

        Report report = new Report(ReportType.Type.INVENTORY, manager.getId(), manager.getWarehouseID(), reportData);
        report = reportRepository.save(report);
        body.put("reportId", report.getId());
        reportData = new JSONObject(body);
        report.setData(reportData);
        reportRepository.save(report);

        return reportData;
    }

    public JSONObject generateTodayReceiptsReport(Authentication auth) throws JsonProcessingException {

        Employee manager = employeeService.getEmployeeByAuthentication(auth);

        String timestamp = LocalDateTime.now().toString();

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("employeeIdCreated", manager.getId());;
        body.put("createdAtWarehouseID", manager.getWarehouseID());
        body.put("timestamp", timestamp);

        List<Object> receiptsData = new ArrayList<>();
        List<Receipt> receipts = receiptService.getByWarehouseIdAndDate(manager.getWarehouseID(), LocalDate.now());
        BigDecimal totalPriceReceipts = BigDecimal.ZERO;
        ObjectMapper mapper = new ObjectMapper();
        for (Receipt receipt : receipts) {

            totalPriceReceipts = totalPriceReceipts.add(receipt.getTotalAmount());
            String receiptString = receiptService.generateReceiptJson(receipt);
            Map<String, Object> parsed = mapper.readValue(receiptString, new TypeReference<Map<String,Object>>() {});
            receiptsData.add(parsed);
        }
        body.put("receiptsData", receiptsData);
        body.put("totalPriceReceipts", totalPriceReceipts);

        JSONObject reportData = new JSONObject(body);

        Report report = new Report(ReportType.Type.SALES, manager.getId(), manager.getWarehouseID(), reportData);
        report = reportRepository.save(report);
        body.put("reportId", report.getId());
        reportData = new JSONObject(body);
        report.setData(reportData);
        reportRepository.save(report);

        return reportData;
    }

    public JSONObject generatePeriodGeneralReport() { return new JSONObject(); }
    public JSONObject generatePeriodReceiptsReport(Authentication auth, LocalDateTime start, LocalDateTime end) throws JsonProcessingException {

        Employee manager = employeeService.getEmployeeByAuthentication(auth);

        String timestamp = LocalDateTime.now().toString();

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("employeeIdCreated", manager.getId());;
        body.put("createdAtWarehouseID", manager.getWarehouseID());
        body.put("timestamp", timestamp);

        List<Object> receiptsData = new ArrayList<>();
        List<Receipt> receipts = receiptService.getByWarehouseIdAndPeriod(manager.getWarehouseID(), start, end);
        BigDecimal totalPriceReceipts = BigDecimal.ZERO;
        ObjectMapper mapper = new ObjectMapper();
        for (Receipt receipt : receipts) {

            totalPriceReceipts = totalPriceReceipts.add(receipt.getTotalAmount());
            String receiptString = receiptService.generateReceiptJson(receipt);
            Map<String, Object> parsed = mapper.readValue(receiptString, new TypeReference<Map<String,Object>>() {});
            receiptsData.add(parsed);
        }
        body.put("receiptsData", receiptsData);
        body.put("totalPriceReceipts", totalPriceReceipts);

        JSONObject reportData = new JSONObject(body);

        Report report = new Report(ReportType.Type.SALES, manager.getId(), manager.getWarehouseID(), reportData);
        report = reportRepository.save(report);
        body.put("reportId", report.getId());
        reportData = new JSONObject(body);
        report.setData(reportData);
        reportRepository.save(report);

        return reportData;
    }
    public JSONObject generatePeriodInventoryReport() { return new JSONObject(); }
    public JSONObject generatePeriodEmployeeReport(Authentication auth, LocalDateTime start, LocalDateTime end) throws JsonProcessingException {

        Employee manager = employeeService.getEmployeeByAuthentication(auth);

        String timestamp = LocalDateTime.now().toString();

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("employeeIdCreated", manager.getId());;
        body.put("createdAtWarehouseID", manager.getWarehouseID());
        body.put("timestamp", timestamp);

        List<Object> employeesData = new ArrayList<>();
        List<Employee> employees = employeeService.getAllActivePeriodEmployeesFromWarehouse(manager.getWarehouseID(), Timestamp.valueOf(start), Timestamp.valueOf(end));
        for (Employee employee : employees) {
            Map<String, Object> employeeData = new LinkedHashMap<>();
            employeeData.put("employeeId", employee.getId());
            employeeData.put("username", employee.getUsername());
            List<String> jobNames = employee.getJobs().stream().map(Job::getName).toList();
            employeeData.put("jobs", jobNames);

            List<Map<String, Object>> loginAudits = loginAuditService.getLoginAuditsByEmployeeAndPeriod(employee, Timestamp.valueOf(start), Timestamp.valueOf(end))
                    .stream().map(audit -> Map.<String, Object>of(
                            "loginTime", audit.getLoginTime(),
                            "ipAddress", audit.getIpAddress(),
                            "successFailure", audit.getSuccessFailure()
                    )).toList();
            employeeData.put("loginAudits", loginAudits);

            List<Map<String, Object>> logoutAudits = logoutAuditService.getLogoutAuditsByEmployeeAndPeriod(employee, Timestamp.valueOf(start), Timestamp.valueOf(end))
                    .stream().map(audit -> Map.<String, Object>of(
                            "logoutTime", audit.getLogoutTime(),
                            "ipAddress", audit.getIpAddress(),
                            "reason", audit.getLogoutReason()
                    )).toList();
            employeeData.put("logoutAudits", logoutAudits);

            if (jobNames.contains("ADMIN")){
                List<Object> settingsChangedData = new ArrayList<>();
                List<SettingsChangeAudit> settingsChangeAudits = settingsChangeAuditService.getSettingsChangeLogsByEmployeeAndPeriod(employee, start, end);
                for (SettingsChangeAudit settingsChangeAudit : settingsChangeAudits) {
                    Map<String, Object> settingChangeData = new LinkedHashMap<>();
                    settingChangeData.put("settingsChangeLogId", settingsChangeAudit.getId());
                    settingChangeData.put("changedAt", settingsChangeAudit.getChangedAt());
                    settingChangeData.put("settingId", settingsChangeAudit.getSettingId());
                    settingChangeData.put("settingsChanged", settingsChangeAudit.getSettingsChanged());

                    settingsChangedData.add(settingChangeData);
                }
                if (!settingsChangedData.isEmpty()) {
                    employeeData.put("settingsChangeLogs", settingsChangeAudits);
                }

            }

            if (jobNames.contains("SALESMAN")){
                List<Object> receiptsData = new ArrayList<>();
                List<Receipt> receipts = receiptService.getByEmployeeAndPeriod(employee, start, end);
                ObjectMapper mapper = new ObjectMapper();
                for (Receipt receipt : receipts) {
                    String receiptString = receiptService.generateReceiptJson(receipt);
                    Map<String, Object> parsed = mapper.readValue(receiptString, new TypeReference<Map<String,Object>>() {});
                    receiptsData.add(parsed);
                }
                if (!receipts.isEmpty()) {
                    employeeData.put("receiptsData", receiptsData);
                }
            }



            employeesData.add(employeeData);
        }
        body.put("employeesData", employeesData);

        JSONObject reportData = new JSONObject(body);

        Report report = new Report(ReportType.Type.EMPLOYEES, manager.getId(), manager.getWarehouseID(), reportData);
        report = reportRepository.save(report);
        body.put("reportId", report.getId());
        reportData = new JSONObject(body);
        report.setData(reportData);
        reportRepository.save(report);

        return reportData;
    }
}
