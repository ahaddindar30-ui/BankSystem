package com.mysite.banking.dao.impl;

import com.mysite.banking.dao.AccountDao;
import com.mysite.banking.model.Account;
import com.mysite.banking.service.DatabaseManager;
import com.mysite.banking.service.exception.UpdateException;
import com.mysite.banking.service.impl.DatabaseManagerImpl;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;

import java.util.List;

public class AccountDaoImpl implements AccountDao {

    private DatabaseManager databaseManager;

    private static final AccountDaoImpl INSTANCE;

    public static AccountDaoImpl getInstance() {
        return INSTANCE;
    }

    static {
        INSTANCE = new AccountDaoImpl();
    }

    private AccountDaoImpl() {
        databaseManager = DatabaseManagerImpl.getInstance();

    }

    @Override
    public Integer saveAccount(Account account) {
        try (Session session = databaseManager.getSession()) {
            session.beginTransaction();
            session.persist(account);
            session.getTransaction().commit();
            return account.getId();
        }
    }

    @Override
    public void updateAccount(Account account) {
        try (Session session = databaseManager.getSession()) {
            session.beginTransaction();
            session.merge(account);
            session.getTransaction().commit();
        } catch (Exception ex) {
            throw new UpdateException("update exception, please retry", ex);
        }
    }

    @Override
    public void deleteAccount(Account account) {
        account.setDeleted(true);
        updateAccount(account);

    }

    @Override
    public Account findAccountById(Integer id) {
        try (Session session = databaseManager.getSession()) {
            return session.find(Account.class, id);
        }

    }


    @Override
    public List<Account> getAllAccounts(Boolean deleted) {
        return getAccountByStatus(null);
    }

    @Override
    public List<Account> getAccountByStatus(Boolean deleted) {
        try (Session session = databaseManager.getSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Account> criteriaQuery = criteriaBuilder.createQuery(Account.class);
            Root<Account> accountRoot = criteriaQuery.from(Account.class);
            criteriaQuery.select(accountRoot);
            if (deleted != null)
                criteriaQuery.where(criteriaBuilder.equal(accountRoot.get("deleted"), deleted));
            return session.createQuery(criteriaQuery).getResultList();
        }


    }

    @Override
    public List<Account> getByCustomerId(Integer customerId) {
        try (Session session = databaseManager.getSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Account> criteriaQuery = criteriaBuilder.createQuery(Account.class);
            Root<Account> accountRoot = criteriaQuery.from(Account.class);

            Predicate accountPredicate = criteriaBuilder.equal(accountRoot.get("customerId"), customerId);
            Predicate deletePredicate = criteriaBuilder.equal(accountRoot.get("deleted"), false);
            Predicate findPredicate = criteriaBuilder.and(accountPredicate, deletePredicate);


            criteriaQuery.select(accountRoot).where(findPredicate);
            return session.createQuery(criteriaQuery).getResultList();
        }
    }
}
