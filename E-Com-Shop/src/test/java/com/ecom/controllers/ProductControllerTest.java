package com.ecom.controllers;

import com.ecom.config.UserService;
import com.ecom.models.Products;
import com.ecom.models.User;
import com.ecom.repository.ProductRepository;
import com.ecom.repository.UserRepository;
import com.ecom.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
class ProductControllerTest {


    @Autowired
    ProductController productController;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ProductRepository productRepository;

    @MockBean
    UserRepository userRepository;

    @MockBean
    ProductService service;

    @MockBean
    UserService userService;

    @Test
    @WithMockUser(username = "j@gmail.com",authorities = {"SELLER"},password = "1234")
    public void addProduct() throws Exception {
        Products products=new Products();
        products.setId(1);
        products.setName("Dell dis");
        products.setSpecifications("ram 4gb");
        products.setType("Laptop");
        products.setDescription("good");
        products.setCategory("Electronics");
        products.setPrice(24000);


        when(service.addProduct(any(Products.class))).thenReturn(products);

        mockMvc.perform(MockMvcRequestBuilders.post("/seller/add-product")
                        .content(new ObjectMapper().writeValueAsString(products))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Dell dis"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.specifications").value("ram 4gb"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("Laptop"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("good"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.category").value("Electronics"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(24000));
    }


}