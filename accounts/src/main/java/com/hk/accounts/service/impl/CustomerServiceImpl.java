package com.hk.accounts.service.impl;

import com.hk.accounts.dto.AccountsDto;
import com.hk.accounts.dto.CardsDto;
import com.hk.accounts.dto.CustomerDetailsDto;
import com.hk.accounts.dto.LoansDto;
import com.hk.accounts.entity.Accounts;
import com.hk.accounts.entity.Customer;
import com.hk.accounts.exception.ResourceNotFoundException;
import com.hk.accounts.mapper.AccountsMapper;
import com.hk.accounts.mapper.CustomerMapper;
import com.hk.accounts.repository.AccountsRepository;
import com.hk.accounts.repository.CustomerRepository;
import com.hk.accounts.service.ICustomerService;
import com.hk.accounts.service.client.CardsFeignClient;
import com.hk.accounts.service.client.LoansFeignClient;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements ICustomerService {

    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;
    private CardsFeignClient cardsFeignClient;
    private LoansFeignClient loansFeignClient;

    @Override
    public CustomerDetailsDto fetchCustomerDetails(String mobileNumber) {
        Customer customer =
                customerRepository.findByMobileNumber(mobileNumber).orElseThrow(() -> new ResourceNotFoundException(
                        "Customer", "mobileNumber", mobileNumber));
        Accounts account =
                accountsRepository.findByCustomerId(customer.getCustomerId())
                        .orElseThrow(() -> new ResourceNotFoundException("Account", "customerId",
                                customer.getCustomerId().toString()));

       CustomerDetailsDto customerDetailsDto = CustomerMapper.mapToCustomerDetailsDto(customer,
                new CustomerDetailsDto());
       customerDetailsDto.setAccountsDto(AccountsMapper.mapToAccountsDto(account, new AccountsDto()));

       ResponseEntity<LoansDto> loansDtoResponseEntity = loansFeignClient.fetchLoanDetails(mobileNumber);
       customerDetailsDto.setLoansDto(loansDtoResponseEntity.getBody());

       ResponseEntity<CardsDto> cardsDtoResponseEntity = cardsFeignClient.fetchCardDetails(mobileNumber);
       customerDetailsDto.setCardsDto(cardsDtoResponseEntity.getBody());

        return customerDetailsDto;
    }
}
