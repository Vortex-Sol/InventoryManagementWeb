package vortex.imwp.services;

import org.json.simple.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import vortex.imwp.dtos.ItemDTO;
import vortex.imwp.dtos.ReportDTO;
import vortex.imwp.mappers.ReportDTOMapper;
import vortex.imwp.models.*;
import vortex.imwp.repositories.ReportRepository;

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
    private final WarehouseService warehouseService;
    private final WarehouseItemService warehouseItemService;

    public ReportService(ReportRepository reportRepository, EmployeeService employeeService, LoginAuditService loginAuditService, SaleService saleService, WarehouseService warehouseService, WarehouseItemService warehouseItemService) {
        this.reportRepository = reportRepository;
        this.employeeService = employeeService;
        this.loginAuditService = loginAuditService;
        this.saleService = saleService;
        this.warehouseService = warehouseService;
        this.warehouseItemService = warehouseItemService;
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

    public JSONObject generateTodaySaleReport(Authentication auth) {

        Employee manager = employeeService.getEmployeeByAuthentication(auth);

        String timestamp = LocalDateTime.now().toString();

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("employeeIdCreated", manager.getId());;
        body.put("createdAtWarehouseID", manager.getWarehouseID());
        body.put("timestamp", timestamp);

        List<Object> salesData = new ArrayList<>();
        List<Sale> sales = saleService.getByWarehouseIdAndDate(manager.getWarehouseID(), LocalDate.now());
        double totalPriceSales = 0.0;
        for (Sale sale : sales) {
            Map<String, Object> saleData = new HashMap<>();
            saleData.put("saleId", sale.getId());
            saleData.put("saleTime", sale.getSaleTime());
            Map<ItemDTO, Integer> saleItems = saleService.getItemsWithQuantity(sale.getId());

            List<Map<String, Object>> itemsList = new ArrayList<>();
            double totalPriceSale = 0.0;

            for (Map.Entry<ItemDTO, Integer> entry : saleItems.entrySet()) {
                ItemDTO itemDTO = entry.getKey();
                Integer quantity = entry.getValue();

                double totalPriceItem = quantity * itemDTO.getPrice();
                totalPriceSale += totalPriceItem;

                Map<String, Object> itemData = new LinkedHashMap<>();
                itemData.put("itemID", itemDTO.getId());
                itemData.put("itemName", itemDTO.getName());
                itemData.put("itemPrice", itemDTO.getPrice());
                itemData.put("itemBarcode", itemDTO.getBarcode());
                itemData.put("itemCategory", itemDTO.getCategory().getName());

                itemData.put("quantity", quantity);
                itemData.put("totalPrice", totalPriceItem);

                itemsList.add(itemData);
            }
            saleData.put("itemsList", itemsList);
            saleData.put("totalPriceSale", totalPriceSale);
            totalPriceSales += totalPriceSale;
            salesData.add(saleData);
        }
        body.put("salesData", salesData);
        body.put("totalPriceSales", totalPriceSales);

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
    public JSONObject generatePeriodSaleReport(Authentication auth, Timestamp start, Timestamp end) {

        Employee manager = employeeService.getEmployeeByAuthentication(auth);

        String timestamp = LocalDateTime.now().toString();

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("employeeIdCreated", manager.getId());;
        body.put("createdAtWarehouseID", manager.getWarehouseID());
        body.put("timestamp", timestamp);

        List<Object> salesData = new ArrayList<>();
        List<Sale> sales = saleService.getByWarehouseIdAndPeriod(manager.getWarehouseID(), start, end);
        double totalPriceSales = 0.0;
        for (Sale sale : sales) {
            Map<String, Object> saleData = new HashMap<>();
            saleData.put("saleId", sale.getId());
            saleData.put("saleTime", sale.getSaleTime());
            Map<ItemDTO, Integer> saleItems = saleService.getItemsWithQuantity(sale.getId());

            List<Map<String, Object>> itemsList = new ArrayList<>();
            double totalPriceSale = 0.0;

            for (Map.Entry<ItemDTO, Integer> entry : saleItems.entrySet()) {
                ItemDTO itemDTO = entry.getKey();
                Integer quantity = entry.getValue();

                double totalPriceItem = quantity * itemDTO.getPrice();
                totalPriceSale += totalPriceItem;

                Map<String, Object> itemData = new LinkedHashMap<>();
                itemData.put("itemID", itemDTO.getId());
                itemData.put("itemName", itemDTO.getName());
                itemData.put("itemPrice", itemDTO.getPrice());
                itemData.put("itemBarcode", itemDTO.getBarcode());
                itemData.put("itemCategory", itemDTO.getCategory().getName());

                itemData.put("quantity", quantity);
                itemData.put("totalPrice", totalPriceItem);

                itemsList.add(itemData);
            }
            saleData.put("itemsList", itemsList);
            saleData.put("totalPriceSale", totalPriceSale);
            totalPriceSales += totalPriceSale;
            salesData.add(saleData);
        }
        body.put("salesData", salesData);
        body.put("totalPriceSales", totalPriceSales);

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
