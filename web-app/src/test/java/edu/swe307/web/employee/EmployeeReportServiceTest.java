
package edu.swe307.web.employee;

import edu.swe307.common.EmployeeRow;
import edu.swe307.common.ExpenseQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployeeReportServiceTest {

    private EmployeeRepository repository;
    private ExpenseQueryService expenseService;
    private EmployeeReportService reportService;

    @BeforeEach
    void setUp() {
        repository = mock(EmployeeRepository.class);
        expenseService = mock(ExpenseQueryService.class);

        reportService = new EmployeeReportService(
                repository,
                expenseService
        );
    }

    private Employee scott() {
        return new Employee(
                7788,
                "SCOTT",
                "JONES",
                new BigDecimal("3000.00"),
                null,
                "RESEARCH",
                "scott.jpg"
        );
    }

    @Test
    void combinesEmployeeAndExpenseData() {
        when(repository.findByName("SCOTT"))
                .thenReturn(Optional.of(scott()));

        when(expenseService.totalExpense(7788))
                .thenReturn(new BigDecimal("1280.45"));

        EmployeeRow report = reportService
                .findReport("SCOTT")
                .orElseThrow();

        assertEquals("SCOTT", report.getEname());
        assertEquals("JONES", report.getMgrName());
        assertEquals("RESEARCH", report.getDepartment());
        assertEquals("scott.jpg", report.getImageFile());

        assertEquals(
                0,
                new BigDecimal("1280.45")
                        .compareTo(report.getTotalExpense())
        );

        verify(expenseService).totalExpense(7788);
    }

    @Test
    void missingEmployeeDoesNotQueryExpenses() {
        when(repository.findByName("UNKNOWN"))
                .thenReturn(Optional.empty());

        assertTrue(reportService
                .findReport("UNKNOWN")
                .isEmpty());

        verifyNoInteractions(expenseService);
    }

    @Test
    void expenseFailurePreservesEmployeeInformation() {
        when(repository.findByName("SCOTT"))
                .thenReturn(Optional.of(scott()));

        when(expenseService.totalExpense(7788))
                .thenThrow(new IllegalStateException(
                        "Spark unavailable"
                ));

        EmployeeRow report = reportService
                .findReport("SCOTT")
                .orElseThrow();

        assertEquals("SCOTT", report.getEname());
        assertEquals("JONES", report.getMgrName());
        assertNull(report.getTotalExpense());
    }

    @Test
    void employeeWithoutExpensesHasZeroTotal() {
        when(repository.findByName("SCOTT"))
                .thenReturn(Optional.of(scott()));

        when(expenseService.totalExpense(7788))
                .thenReturn(new BigDecimal("0.00"));

        EmployeeRow report = reportService
                .findReport("SCOTT")
                .orElseThrow();

        assertNotNull(report.getTotalExpense());

        assertEquals(
                0,
                BigDecimal.ZERO.compareTo(
                        report.getTotalExpense()
                )
        );
    }
}
