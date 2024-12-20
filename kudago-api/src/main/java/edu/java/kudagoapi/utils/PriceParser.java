package edu.java.kudagoapi.utils;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class PriceParser {

    private static final Pattern PRICE_PATTERN = Pattern.compile("\\b\\d+\\b");

    public static BigDecimal toBigDecimal(String input) {
        return new BigDecimal(toLong(input));
    }

    private PriceParser() {
    }

    public static long toLong(String input) {
        long result = Long.MAX_VALUE;
        Matcher matcher = PRICE_PATTERN.matcher(input);
        while (matcher.find()) {
            result = Math.min(result, Long.parseLong(matcher.group()));
        }
        return result == Long.MAX_VALUE ? 0 : result;
    }
}
