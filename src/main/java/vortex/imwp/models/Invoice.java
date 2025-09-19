package vortex.imwp.models;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "Invoice")
public class Invoice {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "File_Name", nullable = false)
	private String fileName;

	@Column(name = "Stored_Path", nullable = false, length = 500)
	private String storedPath;

	@Column(name = "Uploaded_At", nullable = false)
	private Timestamp uploadedAt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "Warehouse_ID", nullable = false)
	private Warehouse warehouse;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "Uploaded_By", nullable = false)
	private Employee uploadedBy;

	public Invoice() {}

	public Invoice(String fileName, String storedPath, Timestamp uploadedAt, Warehouse warehouse, Employee uploadedBy) {
		this.fileName = fileName;
		this.storedPath = storedPath;
		this.uploadedAt = uploadedAt;
		this.warehouse = warehouse;
		this.uploadedBy = uploadedBy;
	}

	public Long getId() { return id; }

	public String getFileName() { return fileName; }
	public void setFileName(String fileName) { this.fileName = fileName; }

	public String getStoredPath() { return storedPath; }
	public void setStoredPath(String storedPath) { this.storedPath = storedPath; }

	public Timestamp getUploadedAt() { return uploadedAt; }
	public void setUploadedAt(Timestamp uploadedAt) { this.uploadedAt = uploadedAt; }

	public Warehouse getWarehouse() { return warehouse; }
	public void setWarehouse(Warehouse warehouse) { this.warehouse = warehouse; }

	public Employee getUploadedBy() { return uploadedBy; }
	public void setUploadedBy(Employee uploadedBy) { this.uploadedBy = uploadedBy; }
}
