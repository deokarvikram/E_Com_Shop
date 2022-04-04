package com.ecom.controllers;

import com.ecom.config.UserService;
import com.ecom.models.Products;
import com.ecom.models.User;
import com.ecom.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.persistence.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    UserController controller;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserRepository userRepository;

    @MockBean
    UserService userService;

    @Test
    public  void addUser() throws Exception {
        User user=new User();

        user.setId(1);
        user.setName("john");
        user.setEmail("j@gmail.com");
        user.setPhone("84736495837");
       // user.setAuthority("SELLER");
        user.setPassword("1234");

        when(userRepository.save(any(User.class))).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/user/add-user")
                .content(new ObjectMapper().writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("john"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("j@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phone").value("84736495837"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password").isEmpty());
    }

    @Test
    public void addProduct()
    {

    }

}