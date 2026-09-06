package com.mysite.banking.dao.impl;

import com.mysite.banking.dao.ATMStockDao;
import com.mysite.banking.model.ATMStock;
import com.mysite.banking.service.DatabaseManager;
import com.mysite.banking.service.exception.UpdateException;
import com.mysite.banking.service.impl.DatabaseManagerImpl;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;


public class ATMStockDaoImpl implements ATMStockDao {

    private DatabaseManager databaseManager;


    private static final ATMStockDaoImpl INSTANCE;

    public static ATMStockDaoImpl getInstance() {
        return INSTANCE;
    }

    static {
        INSTANCE = new ATMStockDaoImpl();
    }

    private ATMStockDaoImpl() {
        databaseManager = DatabaseManagerImpl.getInstance();

    }

    @Override
    public Integer saveATM(ATMStock atmStock) {
        try (Session session = databaseManager.getSession()) {
            session.beginTransaction();
            session.persist(atmStock);
            session.getTransaction().commit();
            return atmStock.getId();
        }
    }

    @Override
    public void updateATM(ATMStock atmStock) {
        try (Session session = databaseManager.getSession()) {
            session.beginTransaction();
            session.merge(atmStock);
            session.getTransaction().commit();
        } catch (Exception ex) {
            throw new UpdateException("update exception, please retry", ex);
        }
    }


    @Override
    public ATMStock findByDenomination(int denomination) {
        try (Session session = databaseManager.getSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<ATMStock> criteriaQuery = criteriaBuilder.createQuery(ATMStock.class);
            Root<ATMStock> atmStockRoot = criteriaQuery.from(ATMStock.class);

            Predicate denominationPredicate = criteriaBuilder.equal(atmStockRoot.get("denomination"), denomination);
            criteriaQuery.select(atmStockRoot).where(denominationPredicate);

            return session.createQuery(criteriaQuery).uniqueResult();
        }
    }


}
