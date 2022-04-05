package com.ecom.controllers;

import com.ecom.models.Authority;
import com.ecom.models.User;
import com.ecom.repository.UserRepository;
import io.swagger.annotations.Api;

import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/user")
@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;



    @ApiOperation(value = "add user")
    @Tag(name="add user",description = "No credentials are required to access this api.Users must be created their profile through this api to create,update and delete their " +
            "own products. Added user will have seller authority. Once user is added user can add,update and delete its products. Make sure users are logged in with user credentials while creating, updating and deleting their product " +
            "Username would be email and password is what user will set while creating profile.")
    @ApiResponses( value={
            @ApiResponse(responseCode ="201", description = "user is created"),
            @ApiResponse(responseCode ="400", description = "user not created"),
            @ApiResponse(responseCode ="500", description = "email or phone duplicated. Other error may be occured")
    })
    @PostMapping("/add-user")
    public ResponseEntity<User> addUser(@RequestBody User newuser)
    {
        String pass=newuser.getPassword();

        newuser.setPassword(passwordEncoder.encode(pass));

        newuser.setAuthority(Authority.SELLER);

        //Authority authority;

        User user=userRepository.save(newuser);


        if(user==null)
            new ResponseEntity<>(user, HttpStatus.BAD_REQUEST);

        user.setPassword(null);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }


}
