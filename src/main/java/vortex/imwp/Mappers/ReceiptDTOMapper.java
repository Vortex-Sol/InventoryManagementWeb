package vortex.imwp.Mappers;

import vortex.imwp.DTOs.ReceiptDTO;
import vortex.imwp.Models.Receipt;

public class ReceiptDTOMapper {
    public static ReceiptDTO map(Receipt receipt) {
        return new ReceiptDTO(
                SaleDTOMapper.map(receipt.getSale()),
                receipt.getTotalAmount(),
                receipt.getPaymentMethod()
        );
    }

    public static Receipt map(ReceiptDTO receiptDTO) {
        return new Receipt(
                SaleDTOMapper.map(receiptDTO.getSale()),
                receiptDTO.getTotalAmount(),
                receiptDTO.getPaymentMethod()
        );
    }
}
