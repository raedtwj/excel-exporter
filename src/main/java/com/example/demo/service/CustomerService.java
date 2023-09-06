package com.example.demo.service;

import com.example.demo.entity.Customer;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

import com.example.demo.repository.CustomerRepo;

public interface CustomerService {

    // Create operation
    Customer createCustomer(Customer customer);

    // Read operation
    List<Customer> readCustomer();

    // Update operation
    Customer updateCustomer(Customer customer, Long id);

    // Delete operation
    void deleteCustomer(Long id);

    // =========================================

    // Export as Xml
    String xmlExtracto(List<Customer> customers);

    // Export as XSSF
    InputStream xssfExtractor(List<Customer> targetList,HashMap<String,String> headers);

}