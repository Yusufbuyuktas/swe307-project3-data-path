
package edu.swe307.web.employee;

import edu.swe307.common.EmployeeRow;
import edu.swe307.common.ExpenseQueryService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class EmployeeReportService {

    private final EmployeeRepository employeeRepository;
    private final ExpenseQueryService expenseQueryService;

    public EmployeeReportService(
            EmployeeRepository employeeRepository,
            ExpenseQueryService expenseQueryService) {
        this.employeeRepository = employeeRepository;
        this.expenseQueryService = expenseQueryService;
    }

    public Optional<EmployeeRow> findReport(String ename) {
        return employeeRepository.findByName(ename)
                .map(this::buildReport);
    }

    private EmployeeRow buildReport(Employee employee) {
        BigDecimal totalExpense;

        try {
            totalExpense = expenseQueryService.totalExpense(
                    employee.getEmpno()
            );
        } catch (RuntimeException exception) {
            totalExpense = null;
        }

        return new EmployeeRow(
                employee.getImageFile(),
                employee.getEname(),
                employee.getMgrName(),
                employee.getSalary(),
                employee.getCommission(),
                employee.getDepartment(),
                totalExpense
        );
    }
}
