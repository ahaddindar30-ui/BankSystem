package com.mysite.banking.view.component;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.mysite.banking.util.ScannerWrapper;

public abstract class BaseConsole {

    protected final ScannerWrapper scannerWrapper;
    protected final ObjectMapper objectMapper;

    public BaseConsole() {
        this.scannerWrapper = ScannerWrapper.getInstance();
        this.objectMapper = new ObjectMapper();
    }
}
