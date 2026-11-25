
package edu.swe307.web.config;

import edu.swe307.common.ExpenseQueryService;
import edu.swe307.query.SparkExpenseQueryService;
import edu.swe307.query.SparkSessionFactory;

import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("real")
public class SparkQueryConfig {

    @Bean
    public SparkSession sparkSession(
            @Value("${p3.spark.master:local[2]}") String master) {

        System.setProperty("spark.master", master);

        return SparkSessionFactory.getInstance();
    }

    @Bean
    public ExpenseQueryService expenseQueryService(
            SparkSession sparkSession) {

        return new SparkExpenseQueryService(sparkSession);
    }
}
