package com.mysite.banking.util;

import com.mysite.banking.model.Amount;
import com.mysite.banking.service.CurrencyConvertService;
import com.mysite.banking.service.impl.CurrencyConvertServiceImpl;

import java.math.BigDecimal;

public class AmountUtil {
    private CurrencyConvertService currencyConvertService;


    private static final AmountUtil INSTANCE;

    public static AmountUtil getInstance() {
        return INSTANCE;
    }

    static {

        INSTANCE = new AmountUtil();
    }

    private AmountUtil() {
        this.currencyConvertService = CurrencyConvertServiceImpl.getInstance();
    }


    public Amount add(Amount firstAmount, Amount secondAmount) {
        BigDecimal convertedAmount = currencyConvertService.convertCurrency(
                secondAmount.getValue(),
                secondAmount.getCurrency(),
                firstAmount.getCurrency()
        );
        return new Amount(
                firstAmount.getId(),
                firstAmount.getCurrency(),
                firstAmount.getValue().add(convertedAmount),
                firstAmount.getVersion()
        );
    }


    public Amount subtract(Amount firstAmount, Amount secondAmount) {
        BigDecimal convertedAmount = currencyConvertService.convertCurrency(
                secondAmount.getValue(),
                secondAmount.getCurrency(),
                firstAmount.getCurrency()
        );
        return new Amount(
                firstAmount.getId(),
                firstAmount.getCurrency(),
                firstAmount.getValue().subtract(convertedAmount),
                firstAmount.getVersion()


        );
    }

    public int compareTo(Amount firstAmount, Amount secondAmount) {
        BigDecimal convertedAmount = currencyConvertService.convertCurrency(
                secondAmount.getValue(),
                secondAmount.getCurrency(),
                firstAmount.getCurrency()
        );
        return firstAmount.getValue().compareTo(convertedAmount);
    }
}
