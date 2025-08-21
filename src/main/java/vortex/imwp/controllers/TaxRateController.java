package vortex.imwp.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
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

	@ModelAttribute("taxRateForm")
	public TaxRate taxRateForm() {
		return new TaxRate();
	}

	@GetMapping
	@PreAuthorize("hasRole('SUPERADMIN')")
	public String list(@RequestParam(value = "q", required = false) String q, Model model) {
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

		return "super/tax-rates";
	}

	@GetMapping("/search")
	@PreAuthorize("hasRole('SUPERADMIN')")
	public String search(@RequestParam("q") String q, Model model) {
		return list(q, model);
	}

	@GetMapping("/edit")
	@PreAuthorize("hasRole('SUPERADMIN')")
	public String edit(@RequestParam("country") String country, Model model) {
		Country.Name c = Country.fromString(country);
		taxRateService.findByCountry(c).ifPresent(tr -> model.addAttribute("taxRateForm", tr));
		return list(null, model);
	}

	@PostMapping("/save")
	@PreAuthorize("hasRole('SUPERADMIN')")
	public String save(@ModelAttribute("taxRateForm") TaxRate form) {
		taxRateService.saveOrUpdateByCountry(form);
		return "redirect:/api/super/taxes";
	}

	@PostMapping("/delete")
	@PreAuthorize("hasRole('SUPERADMIN')")
	public String delete(@RequestParam("country") String country) {
		taxRateService.deleteByCountry(Country.fromString(country));
		return "redirect:/api/super/taxes";
	}
}
