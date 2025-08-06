package vortex.imwp.Services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vortex.imwp.Models.*;
import vortex.imwp.Repositories.ReceiptRepository;
import vortex.imwp.Repositories.SaleRepository;
import vortex.imwp.Repositories.ItemRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Service
public class ReceiptService {

	private final ReceiptRepository receiptRepository;
	private final SaleRepository saleRepository;

	public ReceiptService(ReceiptRepository receiptRepository, SaleRepository saleRepository) {
		this.receiptRepository = receiptRepository;
		this.saleRepository = saleRepository;
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
//			To do: ask if we need to store it
//			receipt.setAmountReceived(amountReceived);
//			receipt.setChangeGiven(amountReceived.subtract(total));
		}

		return receiptRepository.save(receipt);
	}

	public Receipt getReceipt(Long receiptId) {
		return receiptRepository.getById(receiptId);
	}

}
