package vortex.imwp.Services;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vortex.imwp.Models.*;
import vortex.imwp.Repositories.EmployeeRepository;
import vortex.imwp.Repositories.ReceiptRepository;
import vortex.imwp.Repositories.SaleRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;



@Service
public class ReceiptService {

	private final ReceiptRepository receiptRepository;
	private final SaleRepository saleRepository;
	private final EmployeeRepository employeeRepository;

	public ReceiptService(ReceiptRepository receiptRepository, SaleRepository saleRepository, EmployeeRepository employeeRepository) {
		this.receiptRepository = receiptRepository;
		this.saleRepository = saleRepository;
		this.employeeRepository = employeeRepository;
	}

	@Transactional
	public Receipt createReceipt(Sale sale, String paymentMethod, BigDecimal amountReceived) {
		double totalDouble = sale.getSaleItems().stream()
				.mapToDouble(saleItem -> saleItem.getItem().getPrice().doubleValue() * saleItem.getQuantity())
				.sum();

		BigDecimal total = BigDecimal.valueOf(totalDouble);

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
		JSONObject json = new JSONObject();
		json.put("type", "printReceipt");

		JSONObject receiptData = new JSONObject();
		JSONObject header = new JSONObject();
		header.put("operator", "01");
		header.put("cashier", receipt.getSale().getSalesman().getName());
		header.put("invoice", false);
		receiptData.put("header", header);

		JSONArray items = new JSONArray();
		for (SaleItem item : receipt.getSale().getSaleItems()) {
			JSONObject itemJson = new JSONObject();
			itemJson.put("name", item.getItem().getName());
			itemJson.put("quantity", item.getQuantity());
			itemJson.put("unit", "szt");
			itemJson.put("price", item.getItem().getPrice());
			itemJson.put("vatRate", "A"); //  hardcoded for 23% tax
			items.put(itemJson);
		}
		receiptData.put("items", items);

		JSONArray payments = new JSONArray();
		JSONObject payment = new JSONObject();
		payment.put("type", receipt.getPaymentMethod().toLowerCase());
		payment.put("amount", receipt.getTotalAmount());

		if (receipt.getAmountReceived() != null) {
			payment.put("amountReceived", receipt.getAmountReceived());
		}
		if (receipt.getChangeGiven() != null) {
			payment.put("changeGiven", receipt.getChangeGiven());
		}

		payments.put(payment);
		receiptData.put("payments", payments);
		if (receipt.isCancelled()) {
			JSONObject cancellation = new JSONObject();
			cancellation.put("cancelledAt", receipt.getCancelledAt().toString());
			cancellation.put("cancelledBy", receipt.getCancelledBy().getName());
			receiptData.put("cancellation", cancellation);
		}
		JSONObject footer = new JSONObject();
		footer.put("message", "Thank you for shopping!");
		receiptData.put("footer", footer);
		json.put("receipt", receiptData);

		return json.toString(2);
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

}
