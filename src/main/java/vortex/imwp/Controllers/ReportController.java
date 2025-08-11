package vortex.imwp.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import netscape.javascript.JSObject;
import org.json.simple.JSONObject;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vortex.imwp.DTOs.LoginAuditDTO;
import vortex.imwp.DTOs.SaleDTO;
import vortex.imwp.Mappers.EmployeeDTOMapper;
import vortex.imwp.Mappers.LoginAuditDTOMapper;
import vortex.imwp.Mappers.SaleDTOMapper;
import vortex.imwp.Models.Employee;
import vortex.imwp.Models.LoginAudit;
import vortex.imwp.Models.Response;
import vortex.imwp.Services.EmployeeService;
import vortex.imwp.Services.LoginAuditService;
import vortex.imwp.Services.ReportService;
import vortex.imwp.Services.SaleService;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/reports")
public class ReportController {
    private final SaleService saleService;
    private final EmployeeService employeeService;
    private final LoginAuditService loginAuditService;
    private final ReportService reportService;

    public ReportController(SaleService saleService, EmployeeService employeeService, LoginAuditService loginAuditService, ReportService reportService) {
        this.saleService = saleService;
        this.employeeService = employeeService;
        this.loginAuditService = loginAuditService;
        this.reportService = reportService;
    }
    //TODO: GET inventory
    @GetMapping("/inventory")
    public ResponseEntity<Response> getInventoryReport(Authentication authentication){

        Response resp = new vortex.imwp.Models.Response();

        Employee emp = employeeService.getEmployeeByAuthentication(authentication);

//        Map<String, Object> body = new HashMap<>();



//        Optional<Warehouse> warehouseCheck = warehouseService.getWarehouseById(warehouseId);
//        if(warehouseCheck.isEmpty()) {
//            resp.setMessage("Warehouse not found");
//            return ResponseEntity.ok(resp);
//        }
//        System.out.println("warehouse found");
//        Report report = new Report("test", 1L, warehouseId);
//
//        Warehouse warehouse = warehouseCheck.get();
//        System.out.println("warehouse " + warehouse.toString());
//        List<WarehouseItem> warehouseItems = warehouse.getWarehouseItems();
//        List<Map<String, Object>> items = new ArrayList<>();
//        for (WarehouseItem warehouseItem : warehouseItems) {
//            Item item = warehouseItem.getItem();
//            System.out.println("item " + item.toString());
//            items.add(Map.of(
//                    "itemId", item.getId(),
//                    "itemName", item.getName(),
//                    "quantity", warehouseItem.getQuantityInStock()));
//        }
//
//        System.out.println("got items " + items.size());
//        String nowIso = LocalDateTime.now()
//                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
//        Map<String, Object> body = new java.util.HashMap<>(Map.of(
//                "createdAtWarehouseID", warehouseId,
//                "EmployeeIDCreated", 1,
//                "timestamp", nowIso,
//                "items", items
//
//        ));
//
//        ObjectMapper mapper = new ObjectMapper();
//        report.setData(mapper.writeValueAsString(body));
//
//        report = reportService.addReport(report);
//        System.out.println("report " + report.toString());
//
//        body.put("ReportID", report.getId());
//
//        System.out.println("body " + body);

        JSONObject body = reportService.generateTodayEmployeeReport(authentication);
        System.out.println(body.toJSONString());
        resp.setData(body);

        return ResponseEntity.ok(resp);

//        return ResponseEntity.ok(new Response("inventory"));
    }

    @GetMapping("/employees/today")
    public ResponseEntity<Response> getTodayEmployeesReport(Authentication authentication){
        Response resp = new vortex.imwp.Models.Response();

        try{
            JSONObject body = reportService.generateTodayEmployeeReport(authentication);
            System.out.println(body.toJSONString());
            resp.setData(body);
            resp.setMessage("Data found");
            resp.setSuccess(true);

            return ResponseEntity.ok(resp);
        } catch (Exception e){
            resp.setSuccess(false);
            resp.setMessage("Data not found");
            resp.setData(null);

            return ResponseEntity.ok(resp);
        }
    }

    //http://localhost:8080/api/reports/employees/period?start=2025-07-20T00:00:00&end=2025-08-11T23:59:59
    @GetMapping("/employees/period")
    public ResponseEntity<Response> getPeriodEmployeesReport(
            Authentication authentication,

            @RequestParam(value = "start", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime start,

            @RequestParam(value = "end", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime end){
        Response resp = new vortex.imwp.Models.Response();

        try{
            JSONObject body = reportService.generatePeriodEmployeeReport(authentication, Timestamp.valueOf(start), Timestamp.valueOf(end));
            System.out.println(body.toJSONString());
            resp.setData(body);
            resp.setMessage("Data found");
            resp.setSuccess(true);

            return ResponseEntity.ok(resp);
        } catch (Exception e){
            resp.setSuccess(false);
            resp.setMessage("Data not found");
            resp.setData(null);

            return ResponseEntity.ok(resp);
        }
    }


    //TODO: GET sales

    //GET /api/reports/sales?start=2025-01-01T00:00:00&end=2025-01-31T23:59:59
    @GetMapping("/sales")
    public ResponseEntity<Response> getSalesReport(
            @RequestParam(value = "start", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime start,

            @RequestParam(value = "end", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime end)
    {
        Response resp = new vortex.imwp.Models.Response();

        if (start != null && end != null) {
            try{
                Timestamp startTime = Timestamp.valueOf(start);
                Timestamp endTime = Timestamp.valueOf(end);
                Optional<List<SaleDTO>> sales = saleService.getByPeriod(startTime, endTime);
                resp.setSuccess(sales.isPresent());
                resp.setData(saleService.getAll());
                if (resp.isSuccess()) resp.setMessage("Sales by period found");
                else resp.setMessage("Sales by period not found");
                return ResponseEntity.ok(resp);
            } catch (Exception e){}
        }

        Optional<List<SaleDTO>> sales = saleService.getAll();
        resp.setSuccess(sales.isPresent());
        resp.setData(saleService.getAll());
        if (resp.isSuccess()) resp.setMessage("Sales found");
        else resp.setMessage("Sales not found");

        return ResponseEntity.ok(resp);
    }

}
