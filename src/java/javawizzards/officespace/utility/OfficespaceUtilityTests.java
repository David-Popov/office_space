package javawizzards.officespace.utility;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.text.SimpleDateFormat;
import java.util.Date;

class DateUtil {
    public static String formatDate(String inputDate) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy");
            Date date = inputFormat.parse(inputDate);
            return outputFormat.format(date);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid date format");
        }
    }
}

class TaxUtil {
    public static double calculateTax(double amount, double taxRate) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        return amount * taxRate;
    }
}

class UtilityTests {

    @Test
    void testFormatDate() {
        // Arrange
        String inputDate = "2024-12-20";
        String expectedFormattedDate = "20-Dec-2024";

        // Act
        String actualFormattedDate = DateUtil.formatDate(inputDate);

        // Assert
        assertEquals(expectedFormattedDate, actualFormattedDate);
    }

    @Test
    void testFormatDateInvalid() {
        // Arrange
        String inputDate = "invalid-date";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> DateUtil.formatDate(inputDate));
    }

    @Test
    void testCalculateTax() {
        // Arrange
        double amount = 100.0;
        double taxRate = 0.2;
        double expectedTax = 20.0;

        // Act
        double actualTax = TaxUtil.calculateTax(amount, taxRate);

        // Assert
        assertEquals(expectedTax, actualTax, 0.001);
    }

    @Test
    void testCalculateTaxNegativeAmount() {
        // Arrange
        double amount = -100.0;
        double taxRate = 0.2;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> TaxUtil.calculateTax(amount, taxRate));
    }
}

