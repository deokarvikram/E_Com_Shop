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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Dell dis"))
                .andExpect(jsonPath("$.specifications").value("ram 4gb"))
                .andExpect(jsonPath("$.type").value("Laptop"))
                .andExpect(jsonPath("$.description").value("good"))
                .andExpect(jsonPath("$.category").value("Electronics"))
                .andExpect(jsonPath("$.price").value(24000));
    }

    @Test

    public void searchProduct() throws Exception {
        Products products=new Products();
        products.setId(1);
        products.setName("Dell 420");
        products.setSpecifications("ram 4gb");
        products.setType("Laptop");
        products.setDescription("good for coding");
        products.setCategory("Electronics");
        products.setPrice(25000);

        Products products1=new Products();
        products1.setId(2);
        products1.setName("Study table");
        products1.setSpecifications("wooden table");
        products1.setType("table");
        products1.setDescription("good for study");
        products1.setCategory("furniture");
        products1.setPrice(1500);

        Set<Products> set=new HashSet<>();
        set.add(products);
        set.add(products1);
        when(service.getProducts(anyString(),anyInt(),anyInt())).thenReturn(set);

        mockMvc.perform(MockMvcRequestBuilders.get("/search?min=10000&value=table"))
        .andExpect(jsonPath("$[*].id").exists())
                .andExpect(jsonPath("$[*].name",containsInAnyOrder("Dell 420","Study table")))
                .andExpect(jsonPath("$[*].specifications",containsInAnyOrder("ram 4gb","wooden table")))
                .andExpect(jsonPath("$[*].type",containsInAnyOrder("Laptop","table")))
                .andExpect(jsonPath("$[*].description",containsInAnyOrder("good for coding","good for study")))
                .andExpect(jsonPath("$[*].category",containsInAnyOrder("Electronics","furniture")))
                .andExpect(jsonPath("$[*].price",containsInAnyOrder(25000.0,1500.0)));
    }

}