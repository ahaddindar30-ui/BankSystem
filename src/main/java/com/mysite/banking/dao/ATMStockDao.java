package com.mysite.banking.dao;


import com.mysite.banking.model.ATMStock;


public interface ATMStockDao {

    Integer saveATM(ATMStock atmStock);
    void updateATM(ATMStock atmStock);

    ATMStock findByDenomination(int denomination);


}
