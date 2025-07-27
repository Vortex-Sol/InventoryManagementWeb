package vortex.imwp.Controllers;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vortex.imwp.DTOs.SaleDTO;
import vortex.imwp.Models.Response;
import vortex.imwp.Services.SaleService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api/reports")
public class ReportController {
    private final SaleService saleService;

    public ReportController(SaleService saleService) {
        this.saleService = saleService;
    }
    //TODO: GET inventory
    @GetMapping("/inventory")
    public ResponseEntity<Response> getInventoryReport(){

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
