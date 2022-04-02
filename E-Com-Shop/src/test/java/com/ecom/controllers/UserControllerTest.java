package com.ecom.controllers;

import com.ecom.config.UserService;
import com.ecom.models.Products;
import com.ecom.models.User;
import com.ecom.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    UserController userController;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MockMvc mockMvc;

    @Autowired
   private UserService userService;

    @Test
    @WithMockUser(username = "user1", password = "pwd", roles = "USER")
    void addUser() throws Exception {

        User user=new User();

        user.setId(1);
        user.setFirstName("john");
        user.setLastName("smith");
        user.setEmail("joh@gmail.com");
        user.setPhone("3874023847");
        // password 1234 storing in encrypted form
        user.setPassword("$2a$12$KTz5.HKngg5Ir7acFbzVQ.bC9iMhkl15p5anv4XWNuLDfQbxWno7a");
        user.setAuthority("SELLER");

        when(userRepository.save( any(User.class) )).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/user/add-user")
                        .content(new ObjectMapper().writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("firstName").value("john"))
                .andExpect(MockMvcResultMatchers.jsonPath("lastName").value("smith"))
                .andExpect(MockMvcResultMatchers.jsonPath("email").value("joh@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("phone").value("3874023847"))
                .andExpect(MockMvcResultMatchers.jsonPath("password").value("$2a$12$KTz5.HKngg5Ir7acFbzVQ.bC9iMhkl15p5anv4XWNuLDfQbxWno7a"))
                .andExpect(MockMvcResultMatchers.jsonPath("authority").value("SELLER"));


    }
}