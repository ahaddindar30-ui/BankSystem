package com.mySite.banking.facade;

import com.mysite.banking.dto.RealCustomerDto;
import com.mysite.banking.facade.impl.CustomerFacadeImpl;
import com.mysite.banking.model.Customer;
import com.mysite.banking.model.RealCustomer;
import com.mysite.banking.service.CustomerService;
import com.mysite.banking.service.exception.CustomerNotFindException;
import com.mysite.banking.service.exception.DuplicateCustomerException;
import com.mysite.banking.service.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerFacadeTest {
    private CustomerFacadeImpl facade;
    @Mock
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        facade = CustomerFacadeImpl.getInstance(customerService);
    }


    @Test
    void deleteCustomerById_shouldCallService() throws Exception {
        facade.deleteCustomerById(1);

        verify(customerService)
                .deleteCustomerById(1);
    }

    @Test
    void printCustomersByName_shouldReturnCustomers() {

        RealCustomer customer = createCustomer();

        when(customerService.printCustomersByName("Ahad"))
                .thenReturn(List.of(customer));

        var result = facade.printCustomersByName("Ahad");

        assertEquals(1, result.size());
        assertEquals("Ahad", result.getFirst().getName());

        verify(customerService)
                .printCustomersByName("Ahad");
    }

    @Test
    void printCustomerByFamily_shouldReturnCustomers() {

        RealCustomer customer = createCustomer();

        when(customerService.printCustomerByFamily("Din"))
                .thenReturn(List.of(customer));

        var result = facade.printCustomerByFamily("Din");

        assertEquals(1, result.size());
        assertEquals(
                "Din",
                ((RealCustomerDto) result.getFirst()).getFamily()
        );

        verify(customerService)
                .printCustomerByFamily("Din");
    }

    @Test
    void addCustomers_shouldCallService() throws Exception {

        RealCustomerDto dto = createValidDto();
        when(customerService.printCustomersByEmail("ahad@example.com"))
                .thenThrow(new CustomerNotFindException());
        facade.addCustomers(dto);

        verify(customerService)
                .addCustomers(any(Customer.class));
    }

    @Test
    void addCustomers_shouldThrowValidationException_whenNameIsEmpty() throws DuplicateCustomerException {

        RealCustomerDto dto = createValidDto();
        dto.setName("");

        assertThrows(
                ValidationException.class,
                () -> facade.addCustomers(dto)
        );

        verify(
                customerService,
                never()
        ).addCustomers(any(Customer.class));
    }

    @Test
    void addCustomers_shouldThrowValidationException_whenEmailIsInvalid() throws DuplicateCustomerException {

        RealCustomerDto dto = createValidDto();

        dto.setEmail("invalid-email");
        dto.setEmail("");

        assertThrows(
                ValidationException.class,
                () -> facade.addCustomers(dto)
        );

        verify(
                customerService,
                never()
        ).addCustomers(any(Customer.class));
    }

    @Test
    void updateCustomer_shouldUpdateExistingCustomer() throws Exception {

        RealCustomerDto dto = createValidDto();
        dto.setId(1);


        RealCustomer customer = createCustomer();
        customer.setId(1);
        when(customerService.printCustomersByEmail("ahad@example.com"))
                .thenThrow(new CustomerNotFindException());
        when(customerService.getCustomerById(1))
                .thenReturn(customer);

        facade.updateCustomer(dto);

        assertEquals("Ahad", customer.getName());
        assertEquals("Din", customer.getFamily());
        assertEquals("09123456789", customer.getNumber());

        verify(customerService)
                .getCustomerById(1);
    }

    @Test
    void updateCustomer_shouldThrow_whenCustomerNotFound()
            throws Exception {

        RealCustomerDto dto = createValidDto();
        dto.setId(100);
        when(customerService.printCustomersByEmail("ahad@example.com"))
                .thenThrow(new CustomerNotFindException());
        when(customerService.getCustomerById(100))
                .thenThrow(new CustomerNotFindException());

        assertThrows(
                CustomerNotFindException.class,
                () -> facade.updateCustomer(dto)
        );

        verify(customerService)
                .getCustomerById(100);
    }

    @Test
    void getActiveCustomers_shouldReturnCustomers()
            throws Exception {

        RealCustomer customer = createCustomer();

        when(customerService.getActiveCustomers())
                .thenReturn(List.of(customer));

        var result = facade.getActiveCustomers();

        assertEquals(1, result.size());
        assertEquals(
                "Ahad",
                result.getFirst().getName()

        );

        verify(customerService)
                .getActiveCustomers();
    }

    @Test
    void getDeletedCustomers_shouldReturnCustomers()
            throws Exception {

        RealCustomer customer = createCustomer();
        customer.setDeleted(true);

        when(customerService.getDeletedCustomers())
                .thenReturn(List.of(customer));

        var result = facade.getDeletedCustomers();

        assertEquals(1, result.size());
        assertTrue(customer.isDeleted());

        verify(customerService)
                .getDeletedCustomers();
    }

    @Test
    void getCustomerById_shouldReturnCustomer()
            throws Exception {

        RealCustomer customer = createCustomer();
        customer.setId(1);

        when(customerService.getCustomerById(1))
                .thenReturn(customer);

        var result = facade.getCustomerById(1);

        assertEquals(1, result.getId());
        assertEquals("Ahad", result.getName());
        assertEquals(
                "ahad@example.com",
                result.getEmail()
        );

        verify(customerService)
                .getCustomerById(1);
    }

    @Test
    void getCustomerById_shouldThrow_whenCustomerNotFound()
            throws Exception {

        when(customerService.getCustomerById(999))
                .thenThrow(new CustomerNotFindException());

        assertThrows(
                CustomerNotFindException.class,
                () -> facade.getCustomerById(999)
        );

        verify(customerService)
                .getCustomerById(999);
    }



    @Test
    void login_shouldReturnTrue_whenCredentialsAreCorrect() {

        when(customerService.login(
                "ahad@example.com",
                "123456"
        )).thenReturn(true);

        Boolean result = facade.login(
                "ahad@example.com",
                "123456"
        );

        assertTrue(result);

        verify(customerService)
                .login(
                        "ahad@example.com",
                        "123456"
                );
    }

    @Test
    void login_shouldReturnFalse_whenCredentialsAreWrong() {

        when(customerService.login(
                "ahad@example.com",
                "wrong"
        )).thenReturn(false);

        Boolean result = facade.login(
                "ahad@example.com",
                "wrong"
        );

        assertFalse(result);

        verify(customerService)
                .login(
                        "ahad@example.com",
                        "wrong"
                );
    }

    @Test
    void printCustomersByEmail_shouldReturnCustomer()
            throws Exception {

        RealCustomer customer = createCustomer();

        when(customerService.printCustomersByEmail(
                "ahad@example.com"
        )).thenReturn(customer);

        var result = facade.printCustomersByEmail(
                "ahad@example.com"
        );

        assertNotNull(result);
        assertEquals(
                "ahad@example.com",
                result.getEmail()
        );
        assertEquals(
                "Ahad",
                result.getName()
        );

        verify(customerService)
                .printCustomersByEmail(
                        "ahad@example.com"
                );
    }

    @Test
    void printCustomersByEmail_shouldThrow_whenNotFound()
            throws Exception {

        when(customerService.printCustomersByEmail(
                "unknown@example.com"
        )).thenThrow(
                new CustomerNotFindException()
        );

        assertThrows(
                CustomerNotFindException.class,
                () -> facade.printCustomersByEmail(
                        "unknown@example.com"
                )
        );

        verify(customerService)
                .printCustomersByEmail(
                        "unknown@example.com"
                );
    }

    private RealCustomer createCustomer() {

        RealCustomer customer =
                new RealCustomer(
                        "Ahad",
                        "09123456789",
                        "ahad@example.com"
                );

        customer.setFamily("Din");
        customer.setNationalCode("1234567890");
        customer.setPassword("123456");

        return customer;
    }

    private RealCustomerDto createValidDto() {

        RealCustomerDto dto =
                new RealCustomerDto();

        dto.setName("Ahad");
        dto.setNumber("09123456789");
        dto.setEmail("ahad@example.com");
        dto.setPassword("123456");
        dto.setFamily("Din");
        dto.setNationalCode("1234567890");

        return dto;
    }
}



