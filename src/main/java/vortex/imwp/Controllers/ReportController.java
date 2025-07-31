package vortex.imwp.Controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vortex.imwp.DTOs.ReportDTO;
import vortex.imwp.DTOs.SaleDTO;
import vortex.imwp.Mappers.ReportDTOMapper;
import vortex.imwp.Models.*;
import vortex.imwp.Services.ReportService;
import vortex.imwp.Services.SaleService;
import vortex.imwp.Services.WarehouseService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/api/reports")
public class ReportController {
    private final SaleService saleService;
    private final WarehouseService warehouseService;
    private final ReportService reportService;

    public ReportController(SaleService saleService, WarehouseService warehouseService, ReportService reportService) {
        this.saleService = saleService;
        this.warehouseService = warehouseService;
        this.reportService = reportService;
    }
    //TODO: GET inventory
    @GetMapping("/inventory")
    public ResponseEntity<Response> getInventoryReport(
            @RequestParam(value = "id", required = true) Long warehouseId
    ) throws JsonProcessingException {

        Response resp = new vortex.imwp.Models.Response();

        Optional<Warehouse> warehouseCheck = warehouseService.getWarehouseById(warehouseId);
        if(warehouseCheck.isEmpty()) {
            resp.setMessage("Warehouse not found");
            return ResponseEntity.ok(resp);
        }
        System.out.println("warehouse found");
        Report report = new Report("test", 1L, warehouseId);

        Warehouse warehouse = warehouseCheck.get();
        System.out.println("warehouse " + warehouse.toString());
        List<WarehouseItem> warehouseItems = warehouse.getWarehouseItems();
        List<Map<String, Object>> items = new ArrayList<>();
        for (WarehouseItem warehouseItem : warehouseItems) {
            Item item = warehouseItem.getItem();
            System.out.println("item " + item.toString());
            items.add(Map.of(
                    "itemId", item.getId(),
                    "itemName", item.getName(),
                    "quantity", warehouseItem.getQuantityInStock()));
        }

        System.out.println("got items " + items.size());
        String nowIso = LocalDateTime.now()
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        Map<String, Object> body = new java.util.HashMap<>(Map.of(
                "createdAtWarehouseID", warehouseId,
                "EmployeeIDCreated", 1,
                "timestamp", nowIso,
                "items", items

        ));

        ObjectMapper mapper = new ObjectMapper();
        report.setData(mapper.writeValueAsString(body));

        report = reportService.addReport(report);
        System.out.println("report " + report.toString());

        body.put("ReportID", report.getId());

        System.out.println("body " + body);

        resp.setData(body);

        return ResponseEntity.ok(resp);
    }



    //TODO: GET sales

    //GET /api/reports/sales?start=2025-07-20T00:00:00&end=2025-07-21T23:59:59
    //http://localhost:8080/api/reports/sales?start=2025-07-20T00:00:00&end=2025-07-21T23:59:59
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
                resp.setData(sales);
                if (resp.isSuccess()) resp.setMessage("Sales by period found");
                else resp.setMessage("Sales by period not found");
                return ResponseEntity.ok(resp);
            } catch (Exception e){
                System.out.println("Exception during fetching period");
                System.out.println(e.getMessage());
            }
        }

        try{
            Optional<List<SaleDTO>> sales = saleService.getAll();
            resp.setSuccess(sales.isPresent());
            resp.setData(saleService.getAll());
        } catch (Exception e){
            System.out.println("Exception during fetching all");
            System.out.println(e.getMessage());
        }

        if (resp.isSuccess()) resp.setMessage("Sales found");
        else resp.setMessage("Sales not found");

        return ResponseEntity.ok(resp);
    }

}
