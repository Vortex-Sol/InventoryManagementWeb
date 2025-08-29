package vortex.imwp.services;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import vortex.imwp.dtos.SaleDTO;
import vortex.imwp.mappers.SaleDTOMapper;
import vortex.imwp.models.*;
import vortex.imwp.repositories.EmployeeRepository;
import vortex.imwp.repositories.ReceiptRepository;
import vortex.imwp.repositories.SaleRepository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;


@Service
public class ReceiptService {

	private final ReceiptRepository receiptRepository;
	private final SaleRepository saleRepository;
	private final EmployeeRepository employeeRepository;
	private final TaxRateService taxRateService;
    private final WarehouseService warehouseService;
    private final EmployeeService employeeService;

	public ReceiptService(ReceiptRepository receiptRepository, SaleRepository saleRepository, EmployeeRepository employeeRepository, TaxRateService taxRateService,  WarehouseService warehouseService, EmployeeService employeeService) {
		this.receiptRepository = receiptRepository;
		this.saleRepository = saleRepository;
		this.employeeRepository = employeeRepository;
		this.taxRateService = taxRateService;
        this.warehouseService = warehouseService;
        this.employeeService = employeeService;
	}

	@Transactional
	public Receipt createReceipt(Sale sale, String paymentMethod, BigDecimal amountReceived) {
        Optional<Warehouse> warehouse = warehouseService.getWarehouseById(employeeService.getEmployeeByAuthentication(SecurityContextHolder.getContext().getAuthentication()).getWarehouseID());
        if (warehouse.isEmpty()) throw new IllegalArgumentException("Warehouse does not exist");


        BigDecimal total = new BigDecimal(0);
        for (SaleItem item : sale.getSaleItems()){
            BigDecimal itemCost = taxRateService.getBrutto(item.getItem(), warehouse.get());
            BigDecimal totalItemCost = itemCost.multiply(BigDecimal.valueOf(item.getQuantity()));
            total = total.add(totalItemCost);
        }
        System.out.println("[RECEIPT SERVICE] " + total.doubleValue());

		Receipt receipt = new Receipt(sale, total, paymentMethod);
		receipt.setCreatedAt(LocalDateTime.now());

		if ("Cash".equalsIgnoreCase(paymentMethod) && amountReceived != null) {
			if (amountReceived.compareTo(total) < 0) {
				throw new IllegalArgumentException("Amount received is less than total");
			}
			receipt.setAmountReceived(amountReceived);
			receipt.setChangeGiven(amountReceived.subtract(total));
		}

		return receiptRepository.save(receipt);
	}

	public String generateReceiptJson(Receipt receipt) {
        RestTemplate restTemplate = new RestTemplate();

        Map<String, Integer> vatCat = new HashMap<>();
        vatCat.put("A", 0);
        vatCat.put("B", 1);
        vatCat.put("C", 2);
        vatCat.put("D", 3);

        Map<String, Integer> paymentCat = new HashMap<>();
        paymentCat.put("card", 0);
        paymentCat.put("cash", 2);

        Optional<Warehouse> warehouse = warehouseService.getWarehouseById(employeeService.getEmployeeByAuthentication(SecurityContextHolder.getContext().getAuthentication()).getWarehouseID());
        if (warehouse.isEmpty()) throw new IllegalArgumentException("Warehouse does not exist");

        JSONArray lines = new JSONArray();
        for (SaleItem item : receipt.getSale().getSaleItems()){
            BigDecimal price = taxRateService.getBrutto(item.getItem(), warehouse.get()).multiply(BigDecimal.valueOf(100));
            lines.put(new JSONObject()
                    .put("na", item.getItem().getName())
                    .put("il", item.getQuantity())
                    .put("vt", vatCat.get(item.getItem().getCategory().getName()))
                    .put("pr", price.intValue())
            );
        }

        int total = receipt.getTotalAmount().multiply(BigDecimal.valueOf(100)).intValue();
        JSONObject summary = new JSONObject();
        summary.put("to", total);

        JSONArray payments = new JSONArray();
        payments.put(new JSONObject()
                .put("ty", paymentCat.get(receipt.getPaymentMethod().toLowerCase()))
                .put("wa", total)
                .put("na", receipt.getPaymentMethod())
                .put("re", false));

        JSONObject params = new JSONObject()
                .put("lines", lines)
                .put("summary", summary)
                .put("payments", payments);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<String>(params.toString(), headers);

        String url = "http://127.0.0.1:3050/paragon";
        //restTemplate.postForEntity(url, requestEntity, String.class);

        return params.toString(2);
	}

	@Transactional
	public void cancelReceipt(Long receiptId, String username) {
		Receipt receipt = receiptRepository.findById(receiptId)
				.orElseThrow(() -> new IllegalArgumentException("Receipt not found"));

		if (receipt.isCancelled()) {
			throw new IllegalStateException("Receipt is already cancelled");
		}

		Employee employee = employeeRepository.findByUsername(username)
				.orElseThrow(() -> new IllegalArgumentException("Employee not found"));

		receipt.setCancelled(LocalDateTime.now(), employee);
		receiptRepository.save(receipt);
	}


	public Receipt getReceipt(Long receiptId) {
		return receiptRepository.getById(receiptId);
	}

	public List<Receipt> getByEmployeeAndDate(Employee employee, LocalDate date) {
		LocalDateTime start = date.atStartOfDay();
		LocalDateTime end = date.plusDays(1).atStartOfDay();
		return receiptRepository.findReceiptsBySale_SalesmanAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(
				employee, start, end);
	}

	public List<Receipt> getByEmployeeAndPeriod(Employee employee, LocalDateTime start, LocalDateTime end) {
		return receiptRepository.findReceiptsBySale_SalesmanAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(
				employee, start, end);
	}

	public List<Receipt> getByWarehouseIdAndDate(Long warehouseID, LocalDate date) {
		LocalDateTime start = date.atStartOfDay();
		LocalDateTime end = date.plusDays(1).atStartOfDay();
		return receiptRepository.findReceiptsBySale_Salesman_WarehouseIDAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(
				warehouseID, start, end);
	}

	public List<Receipt> getByWarehouseIdAndPeriod(Long warehouseID, LocalDateTime start, LocalDateTime end) {
		return receiptRepository.findReceiptsBySale_Salesman_WarehouseIDAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(
				warehouseID, start, end);
	}

}
