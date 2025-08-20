package vortex.imwp.controllers;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/manager")
public class ManagerController {

	@GetMapping
	public String showReportPage(Model model) {
		return "manager/manager-dashboard";
	}
}
