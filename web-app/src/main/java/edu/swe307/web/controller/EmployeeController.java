
package edu.swe307.web.controller;

import edu.swe307.common.EmployeeRow;
import edu.swe307.web.employee.EmployeeReportService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class EmployeeController {

    private final EmployeeReportService reportService;

    public EmployeeController(EmployeeReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/employee")
    public String findEmployee(
            @RequestParam(required = false) String name,
            Model model) {

        String searchName = name == null ? "" : name.trim();
        model.addAttribute("searchName", searchName);

        if (searchName.isEmpty()) {
            model.addAttribute("error", "Kayıt bulunamadı");
            return "employee";
        }

        Optional<EmployeeRow> report =
                reportService.findReport(searchName);

        if (report.isEmpty()) {
            model.addAttribute("error", "Kayıt bulunamadı");
            return "employee";
        }

        EmployeeRow employee = report.get();
        model.addAttribute("employee", employee);

        if (employee.getTotalExpense() == null) {
            model.addAttribute(
                    "expenseError",
                    "Toplam harcama alınamadı"
            );
        }

        return "employee";
    }
}
