package com.luis.invoice_managementt_api.controller;


import com.luis.invoice_managementt_api.dto.CustomerRequest;
import com.luis.invoice_managementt_api.dto.CustomerResponse;
import com.luis.invoice_managementt_api.service.CustomerService;

import tools.jackson.databind.ObjectMapper;

import com.luis.invoice_managementt_api.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
@Import(GlobalExceptionHandler.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CustomerService customerService;

    @Test
    void shouldCreateCustomer() throws Exception {

        CustomerRequest request = new CustomerRequest(
                "Luiz Teste",
                "luiz.teste@email.com",
                "12345678900"
        );

        CustomerResponse response = new CustomerResponse(
                1L,
                "Luiz Teste",
                "luiz.teste@email.com",
                "12345678900",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(customerService.create(any(CustomerRequest.class)))
                .thenReturn(response);

        mockMvc.perform(
                post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Luiz Teste"))
                .andExpect(jsonPath("$.email").value("luiz.teste@email.com"))
                .andExpect(jsonPath("$.document").value("12345678900"));
    }

    @Test
    void shouldFindCustomerById() throws Exception {

        CustomerResponse response = new CustomerResponse(
                1L,
                "Luiz Teste",
                "luiz.teste@email.com",
                "12345678900",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(customerService.findById(1L))
                .thenReturn(response);

        mockMvc.perform(
                get("/customers/1")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Luiz Teste"))
                .andExpect(jsonPath("$.email").value("luiz.teste@email.com"))
                .andExpect(jsonPath("$.document").value("12345678900"));
    }

    @Test
    void shouldFindAllCustomers() throws Exception {

        CustomerResponse customer1 = new CustomerResponse(
                1L,
                "Luiz Teste",
                "luiz.teste@email.com",
                "12345678900",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        CustomerResponse customer2 = new CustomerResponse(
                2L,
                "João Silva",
                "joao@email.com",
                "98765432100",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(customerService.findAll(any()))
                .thenReturn(
                        new org.springframework.data.domain.PageImpl<>(
                                List.of(customer1, customer2)
                        )
                );

        mockMvc.perform(
                get("/customers")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].name").value("Luiz Teste"))
                .andExpect(jsonPath("$.content[1].id").value(2))
                .andExpect(jsonPath("$.content[1].name").value("João Silva"));
    }

    @Test
    void shouldUpdateCustomer() throws Exception {

        CustomerRequest request = new CustomerRequest(
                "Luiz Atualizado",
                "luiz.atualizado@email.com",
                "12345678900"
        );

        CustomerResponse response = new CustomerResponse(
                1L,
                "Luiz Atualizado",
                "luiz.atualizado@email.com",
                "12345678900",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(customerService.update(
                eq(1L),
                any(CustomerRequest.class)
        )).thenReturn(response);

        mockMvc.perform(
                put("/customers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Luiz Atualizado"))
                .andExpect(jsonPath("$.email")
                        .value("luiz.atualizado@email.com"));
    }

    @Test
    void shouldDeleteCustomer() throws Exception {

        doNothing()
                .when(customerService)
                .delete(1L);

        mockMvc.perform(
                delete("/customers/1")
        )
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldRejectInvalidCustomer() throws Exception {

        CustomerRequest request = new CustomerRequest(
                "",
                "email-invalido",
                ""
        );

        mockMvc.perform(
                post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error")
                        .value("Validation failed"))
                .andExpect(jsonPath("$.errors.name")
                        .value("Name is required"))
                .andExpect(jsonPath("$.errors.email")
                        .value("Email must be valid"))
                .andExpect(jsonPath("$.errors.document")
                        .value("Document is required"));
    }
}
