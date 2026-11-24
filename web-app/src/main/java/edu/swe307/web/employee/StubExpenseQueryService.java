
package edu.swe307.web.employee;

import edu.swe307.common.ExpenseQueryService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Profile("stub")
public class StubExpenseQueryService implements ExpenseQueryService {

    @Override
    public BigDecimal totalExpense(int empno) {
        return new BigDecimal("1234.56");
    }
}
