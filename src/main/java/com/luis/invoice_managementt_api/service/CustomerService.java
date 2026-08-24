package com.luis.invoice_managementt_api.service;

import com.luis.invoice_managementt_api.dto.CustomerRequest;
import com.luis.invoice_managementt_api.dto.CustomerResponse;
import com.luis.invoice_managementt_api.entity.Customer;
import com.luis.invoice_managementt_api.exception.ConflictException;
import com.luis.invoice_managementt_api.exception.ResourceNotFoundException;
import com.luis.invoice_managementt_api.repository.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public CustomerResponse create(CustomerRequest request) {

        if (customerRepository.existsByEmail(request.email())) {
        throw new ConflictException("Email already registered");
        }

        if (customerRepository.existsByDocument(request.document())) {
        throw new ConflictException("Document already registered");
        }

        Customer customer = new Customer();

        customer.setName(request.name());
        customer.setEmail(request.email());
        customer.setDocument(request.document());

        Customer saveCustomer = customerRepository.save(customer);
        return toResponse(saveCustomer);
    }

    public CustomerResponse findById(Long id) {

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        return toResponse(customer);
    }

    public Page<CustomerResponse> findAll(Pageable pageable) {

        return customerRepository.findAll(pageable).map(this::toResponse);
    }

    public CustomerResponse update(Long id, CustomerRequest request) {

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        customer.setName(request.name());
        customer.setEmail(request.email());
        customer.setDocument(request.document());

        Customer updatedCustomer = customerRepository.save(customer);

        return toResponse(updatedCustomer);
    }

    public void delete(Long id) {

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        customerRepository.delete(customer);
    }
    private CustomerResponse toResponse(Customer customer) {

        return new CustomerResponse(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getDocument(),
                customer.getCreatedAt(),
                customer.getUpdatedAt()
        );
    }
}