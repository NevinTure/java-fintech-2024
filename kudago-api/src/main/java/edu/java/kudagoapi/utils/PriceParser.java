package edu.java.kudagoapi.utils;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PriceParser {

    private static final Pattern PRICE_PATTERN = Pattern.compile("\b\\d+\b");

    public static BigDecimal toBigDecimal(String input) {
        return new BigDecimal(toLong(input));
    }

    public static long toLong(String input) {
        long result = 0;
        Matcher matcher = PRICE_PATTERN.matcher(input);
        while (matcher.find()) {
            result = Math.min(result, Long.parseLong(matcher.group()));
        }
        return result;
    }
}
