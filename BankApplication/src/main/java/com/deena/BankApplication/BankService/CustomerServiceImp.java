package com.deena.BankApplication.BankService;

import com.deena.BankApplication.BankRepository.CustomerRepository;
import com.deena.BankApplication.DTO.CustomerDto;
import com.deena.BankApplication.DTO.MailBody;
import com.deena.BankApplication.Entity.Account;
import com.deena.BankApplication.Entity.Customer;
import com.deena.BankApplication.Exception.ResourceNotFoundException;
import com.deena.BankApplication.Mapper.AccountMapper;
import com.deena.BankApplication.Mapper.CustomerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImp implements CustomerService{
    private final CustomerRepository customerRepository;

    private final MailService mailService;

    public CustomerServiceImp(CustomerRepository customerRepository, MailService mailService) {
        this.customerRepository = customerRepository;
        this.mailService = mailService;
    }

    @Override
    @Transactional
    public CustomerDto createCustomer(CustomerDto dto) {
        // Converts DTO to entity (now includes accounts, phones, addresses)
        Customer customer = CustomerMapper.toEntity(dto);

        // Save customer along with accounts and collections
        Customer saved = customerRepository.save(customer);

        // Send welcome email
        MailBody mailBody = MailBody.builder()
                .to(new String[]{saved.getEmail()})
                .subject("Welcome to BankApp!")
                .text("Dear " + saved.getFullName() + ",\n\nYour customer account has been created successfully.")
                .sentDate(new Date())
                .build();

        mailService.sendMessage(mailBody);

        // Convert back to DTO for response
        return CustomerMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerDto getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        return CustomerMapper.toDto(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CustomerDto> getAllCustomers(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Customer> customers = customerRepository.findAll(pageable);
        return customers.map(CustomerMapper::toDto);
    }


    @Override
    @Transactional
    public CustomerDto updateCustomer(Long id, CustomerDto dto) {
        Customer existing = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));

        existing.setFullName(dto.getFullName());
        existing.setEmail(dto.getEmail());
        existing.setPhoneNumbers(dto.getPhoneNumbers());
        existing.setAddresses(dto.getAddresses());

        Customer updated = customerRepository.save(existing);
        return CustomerMapper.toDto(updated);
    }

    @Override
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

//    @Override
//    public void uploadCustomersFromCsv(MultipartFile file) {
//        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
//            br.lines().skip(1).forEach(line -> {
//                String[] data = line.split(",");
//                Customer customer = Customer.builder()
//                        .fullName(data[0])
//                        .email(data[1])
//                        .build();
//                customerRepository.save(customer);
//            });
//        } catch (IOException e) {
//            throw new RuntimeException("Error reading CSV", e);
//        }
//    }
//
//    @Override
//    public String exportCustomersToCsv() {
//        List<Customer> customers = customerRepository.findAll();
//        String header = "Id,FullName,Email\n";
//        String rows = customers.stream()
//                .map(c -> c.getId() + "," + c.getFullName() + "," + c.getEmail())
//                .reduce("", (a, b) -> a.isEmpty() ? b : a + "\n" + b);
//        return header + rows;
//    }
}
