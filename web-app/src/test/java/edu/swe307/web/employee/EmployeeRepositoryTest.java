
package edu.swe307.web.employee;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class EmployeeRepositoryTest {

    private JdbcTemplate jdbcTemplate;
    private EmployeeRepository repository;

    @BeforeEach
    void setUp() {
        jdbcTemplate = mock(JdbcTemplate.class);
        repository = new EmployeeRepository(jdbcTemplate);
    }

    @Test
    void blankNameReturnsEmptyWithoutQuery() {
        assertTrue(repository.findByName("  ").isEmpty());
        assertTrue(repository.findByName(null).isEmpty());

        verifyNoInteractions(jdbcTemplate);
    }

    @Test
    void unknownEmployeeReturnsEmpty() {
        when(jdbcTemplate.query(
                anyString(),
                any(RowMapper.class),
                eq("UNKNOWN")
        )).thenReturn(Collections.emptyList());

        assertTrue(repository.findByName("UNKNOWN").isEmpty());
    }

    @Test
    void searchTrimsWhitespace() {
        when(jdbcTemplate.query(
                anyString(),
                any(RowMapper.class),
                eq("SCOTT")
        )).thenReturn(List.of(
                new Employee(
                        7788,
                        "SCOTT",
                        "JONES",
                        new BigDecimal("3000.00"),
                        null,
                        "RESEARCH",
                        "scott.jpg"
                )
        ));

        Employee result = repository.findByName(" SCOTT ")
                .orElseThrow();

        assertEquals("SCOTT", result.getEname());
        assertEquals(7788, result.getEmpno());
        assertEquals("JONES", result.getMgrName());
    }

    @Test
    void kingCanHaveNullManager() {
        when(jdbcTemplate.query(
                anyString(),
                any(RowMapper.class),
                eq("KING")
        )).thenReturn(List.of(
                new Employee(
                        7839,
                        "KING",
                        null,
                        new BigDecimal("5000.00"),
                        null,
                        "ACCOUNTING",
                        "king.jpg"
                )
        ));

        Employee king = repository.findByName("KING")
                .orElseThrow();

        assertNull(king.getMgrName());
        assertEquals("ACCOUNTING", king.getDepartment());
    }

    @Test
    void turnerHasZeroCommission() {
        when(jdbcTemplate.query(
                anyString(),
                any(RowMapper.class),
                eq("TURNER")
        )).thenReturn(List.of(
                new Employee(
                        7844,
                        "TURNER",
                        "BLAKE",
                        new BigDecimal("1500.00"),
                        new BigDecimal("0.00"),
                        "SALES",
                        "turner.jpg"
                )
        ));

        Employee turner = repository.findByName("TURNER")
                .orElseThrow();

        assertEquals(
                0,
                BigDecimal.ZERO.compareTo(turner.getCommission())
        );
    }
}
