package edu.swe307.streaming;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Converts the draft four-field POS CSV contract to the Cassandra field types. */
public final class ExpenseTransformer {

    private static final String CSV_FIELD = "(\"(?:[^\"\\r\\n]|\"\")*+\"|[^,\"\\r\\n]*)";
    private static final Pattern CSV_ROW =
        Pattern.compile(String.join(",", CSV_FIELD,CSV_FIELD,CSV_FIELD, CSV_FIELD, CSV_FIELD, CSV_FIELD));

    private ExpenseTransformer() {
    }

    public static Optional<Expense> fromCsv(String value) {
        if (value == null) {
            return Optional.empty();
        }

        Matcher fields = CSV_ROW.matcher(value);
        if (!fields.matches()) {
            return Optional.empty();
        }

        String dateTime = decodeField(fields.group(2));
        String description = decodeField(fields.group(3));
        if (dateTime.isBlank() || description.isBlank()) {
            return Optional.empty();
        }

        try {
            int userId = Integer.parseInt(decodeField(fields.group(1)).strip());
            String type = decodeField(fields.group(4));
            int count = Integer.parseInt(decodeField(fields.group(5)).strip());
            BigDecimal payment = new BigDecimal(decodeField(fields.group(6)).strip());

            return Optional.of(new Expense(userId, dateTime, description, type, count, payment));
        } catch (NumberFormatException malformedNumber) {
            return Optional.empty();
        }
    }

    private static String decodeField(String value) {
        if (value.startsWith("\"")) {
            return value.substring(1, value.length() - 1).replace("\"\"", "\"");
        }
        return value;
    }

    public static final class Expense {
        private final int userId;
        private final String dateTime;
        private final String description;
        private final String type;
        private final int count;
        private final BigDecimal payment;

        private Expense(int userId, String dateTime, String description,
                        String type, int count, BigDecimal payment) {
            this.userId = userId;
            this.dateTime = dateTime;
            this.description = description;
            this.type = type;
            this.count = count;
            this.payment = payment;
        }

        public int getUserId() { return userId; }
        public String getDateTime() { return dateTime; }
        public String getDescription() { return description; }
        public String getType() { return type; }
        public int getCount() { return count; }
        public BigDecimal getPayment() { return payment; }
    }
}
