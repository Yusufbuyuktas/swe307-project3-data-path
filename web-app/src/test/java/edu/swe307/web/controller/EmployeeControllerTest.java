
package edu.swe307.web.controller;

import edu.swe307.common.EmployeeRow;
import edu.swe307.web.employee.EmployeeReportService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeReportService reportService;

    private EmployeeRow scott(BigDecimal totalExpense) {
        return new EmployeeRow(
                "scott.jpg",
                "SCOTT",
                "JONES",
                new BigDecimal("3000.00"),
                null,
                "RESEARCH",
                totalExpense
        );
    }

    @Test
    void indexPageLoads() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    void validEmployeeShowsReport() throws Exception {
        when(reportService.findReport("SCOTT"))
                .thenReturn(Optional.of(
                        scott(new BigDecimal("1234.56"))
                ));

        mockMvc.perform(get("/employee")
                        .param("name", "SCOTT"))
                .andExpect(status().isOk())
                .andExpect(view().name("employee"))
                .andExpect(model().attributeExists("employee"))
                .andExpect(model().attributeDoesNotExist("error"));

        verify(reportService).findReport("SCOTT");
    }

    @Test
    void unknownEmployeeShowsNotFoundMessage()
            throws Exception {

        when(reportService.findReport("UNKNOWN"))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/employee")
                        .param("name", "UNKNOWN"))
                .andExpect(status().isOk())
                .andExpect(view().name("employee"))
                .andExpect(model().attribute(
                        "error", "Kayıt bulunamadı"
                ));
    }

    @Test
    void blankNameShowsNotFoundWithoutCallingService()
            throws Exception {

        mockMvc.perform(get("/employee")
                        .param("name", "  "))
                .andExpect(status().isOk())
                .andExpect(model().attribute(
                        "error", "Kayıt bulunamadı"
                ));

        verifyNoInteractions(reportService);
    }

    @Test
    void expenseFailureShowsWarningButKeepsEmployee()
            throws Exception {

        when(reportService.findReport("SCOTT"))
                .thenReturn(Optional.of(scott(null)));

        mockMvc.perform(get("/employee")
                        .param("name", "SCOTT"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("employee"))
                .andExpect(model().attribute(
                        "expenseError",
                        "Toplam harcama alınamadı"
                ));
    }
}
