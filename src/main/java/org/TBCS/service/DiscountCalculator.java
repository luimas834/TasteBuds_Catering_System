package org.TBCS. service;

import org.TBCS.model.Customer;

import java.math.BigDecimal;

public class DiscountCalculator {
    private static final BigDecimal DISCOUNT_PER_ORDER = new BigDecimal("0.02"); // 2% per order
    private static final BigDecimal MAX_DISCOUNT = new BigDecimal("0.15"); // Max 15%

    public BigDecimal calculateDiscount(Customer customer, BigDecimal totalAmount) {
        if (! customer.isRegistered()) {
            return BigDecimal.ZERO;
        }

        int orderCount = customer.getMonthlyOrderCount();
        BigDecimal discountRate = DISCOUNT_PER_ORDER.multiply(BigDecimal.valueOf(orderCount));

        if (discountRate. compareTo(MAX_DISCOUNT) > 0) {
            discountRate = MAX_DISCOUNT;
        }

        return totalAmount.multiply(discountRate);
    }
}