package com.mysite.banking.service.impl;

import com.mysite.banking.service.CurrencyConvertService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

public class CurrencyConvertServiceImpl implements CurrencyConvertService {

    private Map<Currency, BigDecimal> currencyRate;
    private static final CurrencyConvertServiceImpl INSTANCE;

    public static CurrencyConvertServiceImpl getInstance() {
        return INSTANCE;
    }

    static {
        INSTANCE = new CurrencyConvertServiceImpl();
    }


    private CurrencyConvertServiceImpl() {
        currencyRate = new HashMap<>();
        currencyRate.put(Currency.getInstance("USD"), BigDecimal.ONE);
        currencyRate.put(Currency.getInstance("EUR"), new BigDecimal("0.89"));
        currencyRate.put(Currency.getInstance("GBP"), new BigDecimal("0.78"));

    }


    @Override
    public BigDecimal convertCurrency(BigDecimal amount, Currency baseCurrency, Currency destinationCurrency) {
        BigDecimal baseRate = currencyRate.get(baseCurrency);
        BigDecimal destinationRate = currencyRate.get(destinationCurrency);
        BigDecimal convertedAmount = amount.multiply(
                destinationRate.divide(baseRate, 5, RoundingMode.HALF_EVEN)
        );

        return convertedAmount.setScale(2, RoundingMode.HALF_UP);

    }
}
