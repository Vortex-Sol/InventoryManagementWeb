package vortex.imwp.services;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vortex.imwp.dtos.TaxRateDTO;
import vortex.imwp.dtos.WarehouseDTO;

import vortex.imwp.models.*;
import vortex.imwp.repositories.SettingsRepository;
import vortex.imwp.repositories.TaxRateRepository;

import java.sql.Time;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SuperAdminService {
	private final WarehouseService warehouseService;
	private final SettingsRepository settingsRepository;
	private final TaxRateService taxRateService;
	private final SettingsService settingsService;

	public SuperAdminService(WarehouseService warehouseService,
						 SettingsRepository settingsRepository,
							 TaxRateService taxRateService,
							 SettingsService settingsService) {
		this.warehouseService = warehouseService;
		this.settingsRepository = settingsRepository;
		this.taxRateService = taxRateService;
		this.settingsService = settingsService;
	}

	public List<TaxRateDTO> getAllTaxRatesDTO() {
		return taxRateService.findAll().stream()
				.map(tr -> new TaxRateDTO(
						tr.getId(),
						tr.getCountry().name(),
						tr.getStandardRate(),
						tr.getReducedRate(),
						tr.getSuperReducedRate(),
						tr.getNoneRate(),
						tr.getOtherRate(),
						tr.getSettingsDTOs()
				))
				.collect(Collectors.toList());
	}

	@Transactional
	public Warehouse createWarehouseWithTaxRate(
			String phone, String email, String address, Country.Name country,
			String taxRateMode,
			Long existingTaxRateId,
			Country.Name newRateCountry,
			Double std, Double red, Double superRed, Double none, Double other
	) {
		TaxRate taxRate = resolveTaxRate(taxRateMode, existingTaxRateId, newRateCountry, std, red, superRed, none, other);

		Warehouse w = new Warehouse(phone, email, address, country);
		w = warehouseService.addWarehouse(w);

		Settings s = buildDefaultSettings(w, taxRate);
		s = settingsRepository.save(s);

		w.setSettings(s);
		return warehouseService.addWarehouse(w);
	}

	@Transactional
	public void updateWarehouseAndTaxRate(
			Long warehouseId,
			String phone, String email, String address,
			String taxRateMode,
			Long existingTaxRateId,
			Country.Name newRateCountry,
			Double std, Double red, Double superRed, Double none, Double other
	) {
		WarehouseDTO dto = new WarehouseDTO();
		dto.setId(warehouseId);
		dto.setPhone(phone);
		dto.setEmail(email);
		dto.setAddress(address);
		warehouseService.updateWarehouse(warehouseId, dto);

		Settings settings = settingsRepository.findByWarehouse_Id(warehouseId);
		if (settings == null) {
			Warehouse w = warehouseService.getWarehouseById(warehouseId)
					.orElseThrow(() -> new IllegalArgumentException("Warehouse not found"));
			settings = buildDefaultSettings(w, null);
		}

		TaxRate taxRate = resolveTaxRate(taxRateMode, existingTaxRateId, newRateCountry, std, red, superRed, none, other);
		if (taxRate != null) {
			settings.setTaxRate(taxRate);
		}
		settingsRepository.save(settings);
	}

	@Transactional
	public void deleteWarehouse(Long id) throws DataIntegrityViolationException {
		 Settings s = settingsRepository.findByWarehouse_Id(id);
		 if (s != null) settingsRepository.delete(s);
		 warehouseService.deleteWarehouse(id);
	}

	public List<WarehouseDTO> listAllWarehousesDTO() {
		return warehouseService.getAllWarehouses();
	}

	public Optional<Warehouse> getWarehouse(Long id) {
		return warehouseService.getWarehouseById(id);
	}

	public Settings getSettingsByWarehouseId(Long warehouseId) {
		return settingsService.getSettingsByWarehouseId(warehouseId);
	}


	private Settings buildDefaultSettings(Warehouse w, TaxRate tr) {
		Settings s = new Settings();
		s.setWarehouse(w);
		s.setAlertWhenStockIsLow(Boolean.FALSE);
		s.setAutoGenerateReport(Boolean.FALSE);
		s.setAutoGenerateReportTime(null);
		s.setNotifyMinimumCashDiscrepancy(10.00);
		s.setDestroyRefundDataAfterNDays(30);
		s.setCashCountStartTime(Time.valueOf(LocalTime.of(8, 0)));
		s.setCashCountEndTime(Time.valueOf(LocalTime.of(20, 0)));
		s.setAutoGenerateInventoryReportTime(Time.valueOf(LocalTime.of(21, 0)));
		if (tr != null) s.setTaxRate(tr);
		return s;
	}

	private TaxRate resolveTaxRate(String mode,
								   Long existingId,
								   Country.Name country,
								   Double std, Double red, Double superRed, Double none, Double other) {
		if ("new".equalsIgnoreCase(mode)) {
			return taxRateService.createNew(country, std, red, superRed, none, other);
		} else {
			if (existingId == null) return null;
			return taxRateService.findById(existingId)
					.orElseThrow(() -> new IllegalArgumentException("Selected Tax Rate not found"));
		}
	}
}
