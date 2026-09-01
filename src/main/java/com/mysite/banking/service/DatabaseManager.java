package com.mysite.banking.service;


import org.hibernate.Session;

public interface DatabaseManager {

    Session getSession();

}
