package com.deena.BankApplication.Mapper;

import com.deena.BankApplication.DTO.AccountDto;
import com.deena.BankApplication.DTO.CustomerDto;
import com.deena.BankApplication.Entity.Account;
import com.deena.BankApplication.Entity.Customer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CustomerMapper {
    public static Customer toEntity(CustomerDto dto) {
        if(dto == null) return null;

        Customer customer = Customer.builder()
                .id(dto.getId())
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .phoneNumbers(dto.getPhoneNumbers() != null ? dto.getPhoneNumbers() : new ArrayList<>())
                .addresses(dto.getAddresses() != null ? dto.getAddresses() : new ArrayList<>())
                .build();

        // Set accounts if provided in DTO
        if(dto.getAccounts() != null) {
            List<Account> accounts = dto.getAccounts().stream()
                    .map(AccountMapper::toEntity)
                    .peek(acc -> acc.setCustomer(customer))  // important!
                    .toList();
            customer.setAccounts(accounts);
        }

        return customer;
    }

    public static CustomerDto toDto(Customer customer) {
        if(customer == null) return null;

        List<AccountDto> accountDtos = customer.getAccounts() != null
                ? customer.getAccounts().stream().map(AccountMapper::toDto).toList()
                : List.of();

        List<String> phones = customer.getPhoneNumbers() != null ? customer.getPhoneNumbers() : List.of();
        List<String> addresses = customer.getAddresses() != null ? customer.getAddresses() : List.of();

        return new CustomerDto(
                customer.getId(),
                customer.getFullName(),
                customer.getEmail(),
                accountDtos,
                phones,
                addresses
        );
    }
}
