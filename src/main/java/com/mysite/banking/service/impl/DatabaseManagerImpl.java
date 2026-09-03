package com.mysite.banking.service.impl;


import com.mysite.banking.model.Account;
import com.mysite.banking.model.Amount;
import com.mysite.banking.model.LegalCustomer;
import com.mysite.banking.model.RealCustomer;
import com.mysite.banking.service.DatabaseManager;

import org.h2.tools.Server;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;


import java.sql.SQLException;


public class DatabaseManagerImpl implements DatabaseManager {
    private static  DatabaseManagerImpl INSTANCE;

    private final String JDBC_MEM_URL ="jdbc:h2:mem:testDB";

    public static DatabaseManagerImpl getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DatabaseManagerImpl();
        }
        return INSTANCE;
    }



    SessionFactory sessionFactory;

    private DatabaseManagerImpl() {
        String url = System.getProperty("DB_MEM");
        if (url == null || url.isEmpty()) {
            url = "jdbc:h2:./data/BankSystemDB";
        }else {
            url = JDBC_MEM_URL;
        }
        StandardServiceRegistry standardServiceRegistry = new StandardServiceRegistryBuilder()
                .configure()
                .applySetting("hibernate.connection.url", url)
                .build();


        try {
            Server.createTcpServer("-tcpAllowOthers").start();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            Server.createWebServer("-web", "-webAllowOthers").start();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        MetadataSources sources = new MetadataSources(standardServiceRegistry);
        sources.addAnnotatedClass(RealCustomer.class);
        sources.addAnnotatedClass(LegalCustomer.class);
        sources.addAnnotatedClass(Account.class);
        sources.addAnnotatedClass(Amount.class);


        Metadata metadata = sources.getMetadataBuilder().build();

        sessionFactory = metadata.getSessionFactoryBuilder().build();

    }


    @Override
    public Session getSession() {
        return sessionFactory.openSession();
    }
}
