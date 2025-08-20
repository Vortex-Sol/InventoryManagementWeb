package vortex.imwp.controllers;

import org.json.simple.JSONObject;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vortex.imwp.models.Response;
import vortex.imwp.services.ReportService;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/receipts/period")
    @PreAuthorize("hasanyRole('MANAGER','SUPERADMIN')")
    public ResponseEntity<Response> getPeriodReceiptsReport(
            Authentication authentication,

            @RequestParam(value = "start", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime start,

            @RequestParam(value = "end", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime end){
        Response resp = new vortex.imwp.models.Response();

        try{
            JSONObject body = reportService.generatePeriodReceiptsReport(authentication, start, end);
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


    @GetMapping("/receipts/today")
    @PreAuthorize("hasanyRole('MANAGER','SUPERADMIN')")
    public ResponseEntity<Response> getTodaySalesReport(Authentication authentication){
        Response resp = new vortex.imwp.models.Response();

        try{
            JSONObject body = reportService.generateTodayReceiptsReport(authentication);
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

    @GetMapping("/inventory/today")
    @PreAuthorize("hasanyRole('MANAGER','SUPERADMIN')")
    public ResponseEntity<Response> getTodayInventoryReport(Authentication authentication){
        Response resp = new vortex.imwp.models.Response();

        try{
            JSONObject body = reportService.generateTodayInventoryReport(authentication);
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


    @GetMapping("/employees/today")
    @PreAuthorize("hasanyRole('MANAGER','SUPERADMIN')")
    public ResponseEntity<Response> getTodayEmployeesReport(Authentication authentication){
        Response resp = new vortex.imwp.models.Response();

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
    @PreAuthorize("hasanyRole('MANAGER','SUPERADMIN')")
    public ResponseEntity<Response> getPeriodEmployeesReport(
            Authentication authentication,

            @RequestParam(value = "start", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime start,

            @RequestParam(value = "end", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime end){
        Response resp = new vortex.imwp.models.Response();

        try{
            JSONObject body = reportService.generatePeriodEmployeeReport(authentication, start, end);
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

}
