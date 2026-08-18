package com.luis.invoice_managementt_api.service;

import com.luis.invoice_managementt_api.entity.Customer;
import com.luis.invoice_managementt_api.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer create(Customer customer) {
        return customerRepository.save(customer);
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    public Customer findById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    public Customer update(Long id, Customer customer) {
        Customer existingCustomer = findById(id);

        existingCustomer.setName(customer.getName());
        existingCustomer.setEmail(customer.getEmail());
        existingCustomer.setDocument(customer.getDocument());

        return customerRepository.save(existingCustomer);
    }

    public void delete(Long id) {
        Customer existingCustomer = findById(id);

        customerRepository.delete(existingCustomer);
    }
}