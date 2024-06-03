package com.hk.accounts.service.impl;

import com.hk.accounts.constants.AccountsConstants;
import com.hk.accounts.dto.AccountsDto;
import com.hk.accounts.dto.CustomerDto;
import com.hk.accounts.entity.Accounts;
import com.hk.accounts.entity.Customer;
import com.hk.accounts.exception.CustomerAlreadyExistException;
import com.hk.accounts.exception.ResourceNotFoundException;
import com.hk.accounts.mapper.AccountsMapper;
import com.hk.accounts.mapper.CustomerMapper;
import com.hk.accounts.repository.AccountsRepository;
import com.hk.accounts.repository.CustomerRepository;
import com.hk.accounts.service.AccountsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class AccountsServiceImpl implements AccountsService {

    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;

    @Override
    public void createAccount(CustomerDto customerDto) {
        Optional<Customer> optionalCustomer = customerRepository.findByMobileNumber(customerDto.getMobileNumber());
        if (optionalCustomer.isPresent()) {
            throw new CustomerAlreadyExistException("Customer already registered with given mobile number " + customerDto.getMobileNumber());
        }

        Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());

        Customer savedCustomer = customerRepository.save(customer);

        accountsRepository.save(createNewAccount(savedCustomer));
    }

    private Accounts createNewAccount(Customer customer) {
        Accounts newAccount = new Accounts();
        newAccount.setCustomerId(customer.getCustomerId());

        long randomAccNumber = 1000000000L + new Random().nextInt(900000000);
        newAccount.setAccountNumber(randomAccNumber);
        newAccount.setAccountType(AccountsConstants.SAVINGS);
        newAccount.setBranchAddress(AccountsConstants.ADDRESS);

        return newAccount;
    }

    @Override
    public CustomerDto fetchAccount(String mobileNumber) {
        Customer customer =
                customerRepository.findByMobileNumber(mobileNumber).orElseThrow(() -> new ResourceNotFoundException(
                        "Customer", "mobileNumber", mobileNumber));
        Accounts account =
                accountsRepository.findByCustomerId(customer.getCustomerId())
                        .orElseThrow(() -> new ResourceNotFoundException("Account", "customerId",
                                customer.getCustomerId().toString()));

        CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customer, new CustomerDto());
        customerDto.setAccountsDto(AccountsMapper.mapToAccountsDto(account, new AccountsDto()));

        return customerDto;
    }

    @Override
    public boolean updateAccount(CustomerDto customerDto) {
        boolean isUpdated = false;
        AccountsDto accountsDto = customerDto.getAccountsDto();
        if (accountsDto != null) {
            Accounts accounts =
                    accountsRepository.findById(accountsDto.getAccountNumber())
                            .orElseThrow(() -> new ResourceNotFoundException("Account", "AccountNumber",
                                    accountsDto.getAccountNumber().toString()));
            AccountsMapper.mapToAccounts(accountsDto, accounts);
            accounts = accountsRepository.save(accounts);

            Long customerId = accounts.getCustomerId();
            Customer customer =
                    customerRepository.findById(customerId).orElseThrow(() -> new ResourceNotFoundException("Customer"
                            , "CustomerID", customerId.toString()));
            CustomerMapper.mapToCustomer(customerDto, customer);
            customerRepository.save(customer);
            isUpdated = true;
        }

        return isUpdated;
    }

    @Override
    public boolean deleteAccount(String mobileNumber) {
        Customer customer =
                customerRepository.findByMobileNumber(mobileNumber)
                        .orElseThrow(() -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber));
        accountsRepository.deleteByCustomerId(customer.getCustomerId());
        customerRepository.deleteById(customer.getCustomerId());
        return true;
    }
}




















