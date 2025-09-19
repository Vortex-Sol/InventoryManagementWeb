package vortex.imwp.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vortex.imwp.models.Employee;
import vortex.imwp.models.Invoice;
import vortex.imwp.models.Warehouse;
import vortex.imwp.repositories.InvoiceRepository;
import vortex.imwp.repositories.WarehouseRepository;
import vortex.imwp.repositories.EmployeeRepository;

import java.io.IOException;
import java.nio.file.*;
import java.sql.Timestamp;
import java.util.List;

@Service
public class InvoiceService {

	private final InvoiceRepository invoiceRepository;
	private final WarehouseRepository warehouseRepository;
	private final EmployeeRepository employeeRepository;

	private final Path rootLocation = Paths.get("uploads/invoices");

	public InvoiceService(InvoiceRepository invoiceRepository,
						  WarehouseRepository warehouseRepository,
						  EmployeeRepository employeeRepository) throws IOException {
		this.invoiceRepository = invoiceRepository;
		this.warehouseRepository = warehouseRepository;
		this.employeeRepository = employeeRepository;

		if (!Files.exists(rootLocation)) {
			Files.createDirectories(rootLocation);
		}
	}
	public void storeInvoice(MultipartFile file, Long warehouseId, Long employeeId) {
		try {
			if (file.isEmpty()) throw new RuntimeException("File is empty");

			Warehouse warehouse = warehouseRepository.findById(warehouseId)
					.orElseThrow(() -> new RuntimeException("Warehouse not found"));

			Employee employee = employeeRepository.findById(employeeId)
					.orElseThrow(() -> new RuntimeException("Employee not found"));

			String uniqueFileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
			Path destinationFile = rootLocation.resolve(Paths.get(uniqueFileName))
					.normalize().toAbsolutePath();

			Files.copy(file.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);

			Invoice invoice = new Invoice(
					file.getOriginalFilename(),
					destinationFile.toString(),
					new Timestamp(System.currentTimeMillis()),
					warehouse,
					employee
			);

			invoiceRepository.save(invoice);

		} catch (IOException e) {
			throw new RuntimeException("Failed to store file " + file.getOriginalFilename(), e);
		}
	}

	public List<Invoice> getInvoicesForWarehouse(Long warehouseId) {
		return invoiceRepository.findByWarehouse_Id(warehouseId);
	}

	public Invoice getInvoiceById(Long id, Long warehouseId) {
		return invoiceRepository.findById(id)
				.filter(inv -> inv.getWarehouse().getId().equals(warehouseId))
				.orElseThrow(() -> new RuntimeException("Invoice not found or access denied"));
	}

	public Path loadFile(String storedPath) {
		return Paths.get(storedPath);
	}
}
