package vortex.imwp.controllers;

import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vortex.imwp.dtos.TaxRateDTO;
import vortex.imwp.models.Country;
import vortex.imwp.models.TaxRate;
import vortex.imwp.services.TaxRateService;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/taxrates")
public class TaxRateController {

	private final TaxRateService taxRateService;

	public TaxRateController(TaxRateService taxRateService) {
		this.taxRateService = taxRateService;
	}

	@ModelAttribute("countries")
	public Country.Name[] countries() {
		return Country.Name.values();
	}

	private void populateTaxRates(Model model, String q) {
		List<TaxRate> all = taxRateService.findAll();
		if (StringUtils.hasText(q)) {
			final String s = q.toLowerCase(Locale.ROOT);
			all = all.stream()
					.filter(tr -> tr.getCountry() != null && tr.getCountry().name().toLowerCase(Locale.ROOT).contains(s))
					.collect(Collectors.toList());
		}
		model.addAttribute("taxRates", all.stream()
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
				.collect(Collectors.toList()));
	}

	private void ensureTaxRateFormExists(Model model) {
		if (!model.containsAttribute("taxRateForm")) {
			model.addAttribute("taxRateForm", new TaxRate());
		}
	}


	@GetMapping
	@PreAuthorize("hasRole('SUPERADMIN')")
	public String list(@RequestParam(value = "q", required = false) String q, Model model) {
		populateTaxRates(model, q);
		ensureTaxRateFormExists(model);
		return "super/tax-rates";
	}

	@GetMapping("/search")
	@PreAuthorize("hasRole('SUPERADMIN')")
	public String search(@RequestParam("q") String q, Model model) {
		return list(q, model);
	}

	@GetMapping("/edit")
	@PreAuthorize("hasRole('SUPERADMIN')")
	public String edit(@RequestParam("country") String country, Model model, RedirectAttributes ra) {
		Country.Name c = Country.fromString(country);
		if (c == null) {
			ra.addFlashAttribute("error", "Unknown country: " + country);
			return "redirect:/api/taxrates";
		}
		taxRateService.findByCountry(c).ifPresentOrElse(
				tr -> model.addAttribute("taxRateForm", tr),
				() -> model.addAttribute("taxRateForm", new TaxRate()) // fallback to empty form
		);

		return list(null, model);
	}

	@PostMapping("/save")
	@PreAuthorize("hasRole('SUPERADMIN')")
	public String save(@Valid @ModelAttribute("taxRateForm") TaxRate form,
					   BindingResult binding,
					   Model model,
					   RedirectAttributes ra) {

		if (form.getCountry() == null) {
			binding.rejectValue("country", "country.null", "Country is required");
		}

		if (binding.hasErrors()) {
			populateTaxRates(model, null);
			return "super/tax-rates";
		}

		taxRateService.saveOrUpdateByCountry(form);
		ra.addFlashAttribute("success", "Tax rate saved.");
		return "redirect:/api/super/taxes";
	}

	@PostMapping("/delete")
	@PreAuthorize("hasRole('SUPERADMIN')")
	public String delete(@RequestParam("country") String country, RedirectAttributes ra) {
		Country.Name c = Country.fromString(country);
		if (c == null) {
			ra.addFlashAttribute("error", "Unknown country: " + country);
			return "redirect:/api/taxrates";
		}
		try {
			taxRateService.hardDeleteTaxRateAndDependents(c);
			ra.addFlashAttribute("success", "Tax rate deleted.");
		} catch (Exception ex) {
			ra.addFlashAttribute("error", "Delete failed: " + ex.getMessage());
		}
		return "redirect:/api/super/taxes";
	}
}
