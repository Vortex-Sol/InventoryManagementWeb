package vortex.imwp.controllers;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vortex.imwp.models.Employee;
import vortex.imwp.models.Invoice;
import vortex.imwp.services.EmployeeService;
import vortex.imwp.services.InvoiceService;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.List;

@Controller
@RequestMapping("/api/invoice")
public class InvoiceController {

	private final EmployeeService employeeService;
	private final InvoiceService invoiceService;

	public InvoiceController(EmployeeService employeeService, InvoiceService invoiceService) {
		this.employeeService = employeeService;
		this.invoiceService = invoiceService;
	}

	@GetMapping
	@PreAuthorize("hasAnyRole('MANAGER','ADMIN','SUPERADMIN')")
	public String invoicePage(Authentication auth, Model model,
							  @ModelAttribute("errorMessage") String errorMessage) {
		Employee employee = employeeService.getEmployeeByAuthentication(auth);
		List<Invoice> invoices = invoiceService.getInvoicesForWarehouse(employee.getWarehouseID());

		model.addAttribute("invoices", invoices);

		if (errorMessage != null && !errorMessage.isEmpty()) {
			model.addAttribute("errorMessage", errorMessage);
		}

		return "inventory/invoice";
	}

	@PostMapping("/upload")
	@PreAuthorize("hasAnyRole('MANAGER','ADMIN','SUPERADMIN')")
	public String uploadInvoice(@RequestParam("file") MultipartFile file,
								Authentication auth,
								RedirectAttributes redirectAttributes) {
		try {
			Employee employee = employeeService.getEmployeeByAuthentication(auth);
			// ✅ no more LocalDateTime.now()
			invoiceService.storeInvoice(file, employee.getWarehouseID(), employee.getId());
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("errorMessage",
					"Upload failed: " + e.getMessage());
		}
		return "redirect:/api/invoice";
	}

	@GetMapping("/download/{id}")
	@PreAuthorize("hasAnyRole('MANAGER','ADMIN','SUPERADMIN')")
	public ResponseEntity<Resource> downloadInvoice(@PathVariable Long id, Authentication auth) throws MalformedURLException {
		Employee employee = employeeService.getEmployeeByAuthentication(auth);
		Invoice invoice = invoiceService.getInvoiceById(id, employee.getWarehouseID());

		Path filePath = invoiceService.loadFile(invoice.getStoredPath());
		Resource resource = new UrlResource(filePath.toUri());

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + invoice.getFileName() + "\"")
				.body(resource);
	}

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public String handleMaxUploadSize(MaxUploadSizeExceededException e,
									  RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("errorMessage",
				"File too large! Maximum allowed size is 20MB.");
		return "redirect:/api/invoice";
	}
}
