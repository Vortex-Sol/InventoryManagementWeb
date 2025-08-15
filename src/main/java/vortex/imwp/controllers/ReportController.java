package vortex.imwp.controllers;

import org.json.simple.JSONObject;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vortex.imwp.dtos.SaleDTO;
import vortex.imwp.models.Employee;
import vortex.imwp.models.Response;
import vortex.imwp.services.EmployeeService;
import vortex.imwp.services.LoginAuditService;
import vortex.imwp.services.ReportService;
import vortex.imwp.services.SaleService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

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

    @GetMapping("/receipts/period")
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
    public ResponseEntity<Response> getTodayReceiptsReport(Authentication authentication){
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
