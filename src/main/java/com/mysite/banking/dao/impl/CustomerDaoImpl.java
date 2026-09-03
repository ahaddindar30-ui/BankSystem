package com.mysite.banking.dao.impl;

import com.mysite.banking.dao.CustomerDao;
import com.mysite.banking.model.Customer;
import com.mysite.banking.model.RealCustomer;
import com.mysite.banking.service.DatabaseManager;
import com.mysite.banking.service.exception.UpdateException;
import com.mysite.banking.service.impl.DatabaseManagerImpl;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;

import java.util.List;


public class CustomerDaoImpl implements CustomerDao {

    private DatabaseManager databaseManager;

    private static final CustomerDaoImpl INSTANCE;

    public static CustomerDaoImpl getInstance() {
        return INSTANCE;
    }

    static {
        INSTANCE = new CustomerDaoImpl();
    }

    private CustomerDaoImpl() {
        databaseManager = DatabaseManagerImpl.getInstance();

    }

    @Override
    public Integer saveCustomer(Customer customer) {
        try (Session session = databaseManager.getSession()) {
            session.beginTransaction();
            session.persist(customer);
            session.getTransaction().commit();
            return customer.getId();
        }

    }

    @Override
    public void updateCustomer(Customer customer) {
        try (Session session = databaseManager.getSession()) {
            session.beginTransaction();
            session.merge(customer);
            session.getTransaction().commit();
        } catch (Exception ex) {
            throw new UpdateException("update exception, please retry", ex);
        }
    }

    @Override
    public void deleteCustomer(Customer customer) {
        customer.setDeleted(true);
        updateCustomer(customer);
    }

    @Override
    public Customer findCustomerById(Integer id) {
        try (Session session = databaseManager.getSession()) {
            return session.find(Customer.class, id);
        }
    }

    @Override
    public List<Customer> getCustomerByStatus(Boolean deleted) {
        try (Session session = databaseManager.getSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Customer> criteriaQuery = criteriaBuilder.createQuery(Customer.class);
            Root<Customer> customerRoot = criteriaQuery.from(Customer.class);
            criteriaQuery.select(customerRoot);
            if (deleted != null)
                criteriaQuery.where(criteriaBuilder.equal(customerRoot.get("deleted"), deleted));
            return session.createQuery(criteriaQuery).getResultList();
        }

    }

    @Override
    public List<Customer> getAllCustomers(Boolean deleted) {
        return getCustomerByStatus(null);
    }

    @Override
    public List<Customer> getCustomerByName(String name) {
        try (Session session = databaseManager.getSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Customer> criteriaQuery = criteriaBuilder.createQuery(Customer.class);
            Root<Customer> customerRoot = criteriaQuery.from(Customer.class);

            Predicate namePredicate = criteriaBuilder.equal(customerRoot.get("name"), name);
            Predicate deletePredicate = criteriaBuilder.equal(customerRoot.get("deleted"), false);
            Predicate findPredicate = criteriaBuilder.and(namePredicate, deletePredicate);


            criteriaQuery.select(customerRoot).where(findPredicate);
            return session.createQuery(criteriaQuery).getResultList();
        }
    }

    @Override
    public List<Customer> getCustomerByFamily(String family) {
        try (Session session = databaseManager.getSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Customer> criteriaQuery = criteriaBuilder.createQuery(Customer.class);
            Root<RealCustomer> customerRoot = criteriaQuery.from(RealCustomer.class);

            Predicate namePredicate = criteriaBuilder.equal(customerRoot.get("family"), family);
            Predicate deletePredicate = criteriaBuilder.equal(customerRoot.get("deleted"), false);
            Predicate findPredicate = criteriaBuilder.and(namePredicate, deletePredicate);


            criteriaQuery.select(customerRoot).where(findPredicate);
            return session.createQuery(criteriaQuery).getResultList();
        }
    }

    @Override
    public Customer getCustomerByEmail(String email) {
        try (Session session = databaseManager.getSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Customer> criteriaQuery = criteriaBuilder.createQuery(Customer.class);
            Root<Customer> customerRoot = criteriaQuery.from(Customer.class);

            Predicate namePredicate = criteriaBuilder.equal(customerRoot.get("email"), email);
            Predicate deletePredicate = criteriaBuilder.equal(customerRoot.get("deleted"), false);
            Predicate findPredicate = criteriaBuilder.and(namePredicate, deletePredicate);


            criteriaQuery.select(customerRoot).where(findPredicate);

            TypedQuery<Customer> query = session.createQuery(criteriaQuery);
            query.setMaxResults(1);
            List<Customer> customers = query.getResultList();
            return customers.isEmpty() ? null : customers.get(0);
        }
    }
}
