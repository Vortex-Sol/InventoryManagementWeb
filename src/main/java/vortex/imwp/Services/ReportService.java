package vortex.imwp.Services;

import org.json.simple.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import vortex.imwp.DTOs.ItemDTO;
import vortex.imwp.DTOs.LoginAuditDTO;
import vortex.imwp.DTOs.ReportDTO;
import vortex.imwp.DTOs.SaleDTO;
import vortex.imwp.Mappers.LoginAuditDTOMapper;
import vortex.imwp.Mappers.ReportDTOMapper;
import vortex.imwp.Mappers.SaleDTOMapper;
import vortex.imwp.Models.*;
import vortex.imwp.Repositories.ReportRepository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportService {
    private final ReportRepository reportRepository;
    private final EmployeeService employeeService;
    private final LoginAuditService loginAuditService;
    private final SaleService saleService;

    public ReportService(ReportRepository reportRepository, EmployeeService employeeService, LoginAuditService loginAuditService, SaleService saleService) {
        this.reportRepository = reportRepository;
        this.employeeService = employeeService;
        this.loginAuditService = loginAuditService;
        this.saleService = saleService;
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

    public JSONObject generateTodayEmployeeReport(Authentication auth) {

        Employee manager = employeeService.getEmployeeByAuthentication(auth);

        String timestamp = LocalDateTime.now().toString();

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("employeeIdCreated", manager.getId());;
        body.put("createdAtWarehouseID", manager.getWarehouseID());
        body.put("timestamp", timestamp);

        List<Object> employeesData = new ArrayList<>();
        List<Employee> employees = employeeService.getAllEmployeesFromWarehouseWithJob(manager.getWarehouseID(), "SALESMAN");
        for (Employee employee : employees) {
            Map<String, Object> employeeData = new LinkedHashMap<>();
            employeeData.put("employeeId", employee.getId());
            employeeData.put("username", employee.getUsername());

            List<Map<String, Object>> loginAudits = loginAuditService.getLoginAuditsByEmployeeAndDate(employee, LocalDate.now())
                    .stream().map(audit -> Map.<String, Object>of(
                            "loginDate", audit.getLoginTime(),
                            "ipAddress", audit.getIpAddress(),
                            "successFailure", audit.getSuccessFailure()
                    )).toList();
            employeeData.put("loginAudits", loginAudits);

            List<Object> salesData = new ArrayList<>();
            List<Sale> sales = saleService.getByEmployeeAndDate(employee, LocalDate.now());
            for (Sale sale : sales) {
                Map<String, Object> saleData = new HashMap<>();
                saleData.put("saleId", sale.getId());
                saleData.put("saleTime", sale.getSaleTime());

                Map<ItemDTO, Integer> saleItems = saleService.getItemsWithQuantity(sale.getId());
                saleData.put("saleItems", saleItems);
                salesData.add(saleData);
            }
            employeeData.put("salesData", salesData);

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
    public JSONObject generateTodaySaleReport() { return new JSONObject(); }
    public JSONObject generateTodayInventoryReport() { return new JSONObject(); }

    public JSONObject generatePeriodGeneralReport() { return new JSONObject(); }
    public JSONObject generatePeriodSaleReport() { return new JSONObject(); }
    public JSONObject generatePeriodInventoryReport() { return new JSONObject(); }
    public JSONObject generatePeriodEmployeeReport(Authentication auth, Timestamp start, Timestamp end) {

        Employee manager = employeeService.getEmployeeByAuthentication(auth);

        String timestamp = LocalDateTime.now().toString();

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("employeeIdCreated", manager.getId());;
        body.put("createdAtWarehouseID", manager.getWarehouseID());
        body.put("timestamp", timestamp);

        List<Object> employeesData = new ArrayList<>();
        List<Employee> employees = employeeService.getAllEmployeesFromWarehouseWithJob(manager.getWarehouseID(), "SALESMAN");
        for (Employee employee : employees) {
            Map<String, Object> employeeData = new LinkedHashMap<>();
            employeeData.put("employeeId", employee.getId());
            employeeData.put("username", employee.getUsername());

            List<Map<String, Object>> loginAudits = loginAuditService.getLoginAuditsByEmployeeAndPeriod(employee, start, end)
                    .stream().map(audit -> Map.<String, Object>of(
                            "loginDate", audit.getLoginTime(),
                            "ipAddress", audit.getIpAddress(),
                            "successFailure", audit.getSuccessFailure()
                    )).toList();
            employeeData.put("loginAudits", loginAudits);

            List<Object> salesData = new ArrayList<>();
            List<Sale> sales = saleService.getByEmployeeAndPeriod(employee, start, end);
            for (Sale sale : sales) {
                Map<String, Object> saleData = new HashMap<>();
                saleData.put("saleId", sale.getId());
                saleData.put("saleTime", sale.getSaleTime());

                Map<ItemDTO, Integer> saleItems = saleService.getItemsWithQuantity(sale.getId());
                saleData.put("saleItems", saleItems);
                salesData.add(saleData);
            }
            employeeData.put("salesData", salesData);

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
