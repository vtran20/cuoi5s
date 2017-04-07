package com.easysoft.ecommerce.util;

import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;

/**
 * Use for showing money range:
 * For example:
 * Input data should be
 * 100000 - 200000
 * 2.000.000 - 5.000.000
 *
 * Result:
 *
 * 100.000 - 200.000 VND
 * 2.000.000 - 5.000.000
 *
 * User: vtran
 * Date: Aug 4, 2010
 * Time: 11:44:11 AM
 *
 */
public class MoneyRange {

    private Money lower = null;
    private Money greater = null;

    private void validate(){
        if(lower != null && greater != null && lower.getAmount().longValue() > greater.getAmount().longValue()) {
            throw new IllegalArgumentException("The money range is invalid");
        }
    }

    /**
     * Constructor with the range of money
     *
     * @param min	The lower money.
     * @param max	The greater money.
     * @param currency	The money currency.
     * @param currencyFormat	The money currency format.
     */
    public MoneyRange(BigDecimal min, BigDecimal max, String currency, String currencyFormat) {
        if (min != null && min.longValue() >= 0) {
            lower = new Money(min, currency, currencyFormat);
        }
        if (max != null && max.longValue() >= 0) {
            greater = new Money(max, currency, currencyFormat);
        }
        this.validate();
    }

    public MoneyRange(String range, String currency, String currencyFormat) {
        if (StringUtils.isEmpty(range))
            range = "-";
        String money[] = range.split("-");
        if (money.length >= 1 && !StringUtils.isEmpty(money[0].trim())) {
            lower = Money.valueOf(money[0].trim(), currency, currencyFormat);
        }
        if (money.length >= 2 && !StringUtils.isEmpty(money[1].trim())) {
            greater = Money.valueOf(money[1].trim(), currency, currencyFormat);
        }
        this.validate();
    }

    /**
     * Returns the String representation of the range of money. Showing currency unit by default
     * Input data should be
     * 100000 - 200000
     * 2.000.000 - 5.000.000
     * 100.000
     *
     * Result:
     * 100.000 - 200.000 VND
     * 2.000.000 - 5.000.000
     * 100.000 VND
     * @return The amount and currency in a money format.
     */
    public String toString() {
        if (lower != null) {
            if (greater != null) {
                if (lower.equals(greater)) {
                    return lower.toString();
                } else {
                    return lower.getMoneyValue() + " - " + greater.getMoneyValue() + " " + lower.getCurrency();
                }
            } else {
                return lower.toString();
            }
        } else {
            if (greater != null) {
                return greater.toString();
            } else {
                return "";
            }
        }
    }

    public static MoneyRange valueOf (String range, String currency, String currencyFormat) {
        return new MoneyRange (range, currency, currencyFormat);
    }
}
