package vortex.imwp.Mappers;

import vortex.imwp.DTOs.ReceiptDTO;
import vortex.imwp.Models.Receipt;

public class ReceiptDTOMapper {
    public static ReceiptDTO map(Receipt receipt) {
        return new ReceiptDTO(
                receipt.getId(),
                SaleDTOMapper.map(receipt.getSale()),
                receipt.getTotalAmount(),
                receipt.getCreatedAt(),
                receipt.getPaymentMethod(),
                receipt.getAmountReceived(),
                receipt.getChangeGiven(),
                receipt.isCancelled(),
                receipt.getCancelledAt(),
                receipt.getCancelledBy() != null ? EmployeeDTOMapper.map(receipt.getCancelledBy()) : null
        );
    }

    public static Receipt map(ReceiptDTO dto) {
        Receipt receipt = new Receipt(
                dto.getSale() != null ? SaleDTOMapper.map(dto.getSale()) : null,
                dto.getTotalAmount(),
                dto.getPaymentMethod()
        );
        receipt.setId(dto.getId());
        receipt.setCreatedAt(dto.getCreatedAt());
        receipt.setAmountReceived(dto.getAmountReceived());
        receipt.setChangeGiven(dto.getChangeGiven());

        if (dto.isCancelled()) {
            receipt.setCancelled(dto.getCancelledAt(),
                    dto.getCancelledBy() != null ? EmployeeDTOMapper.map(dto.getCancelledBy()) : null);
        }

        return receipt;
    }
}
