package com.luis.invoice_managementt_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.luis.invoice_managementt_api.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
    boolean existsByEmail(String email);

    boolean existsByDocument(String document);
}
