package com.deena.BankApplication.BankService;

import com.deena.BankApplication.DTO.CustomerDto;
import com.deena.BankApplication.Entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface CustomerService {

    CustomerDto createCustomer(CustomerDto dto);

    CustomerDto getCustomerById(Long id);

    Page<CustomerDto> getAllCustomers(int page, int size, String sortBy, String sortDir);

    CustomerDto updateCustomer(Long id, CustomerDto dto);

    void deleteCustomer(Long id);

  //  void uploadCustomersFromCsv(MultipartFile file);

   // String exportCustomersToCsv();
}
