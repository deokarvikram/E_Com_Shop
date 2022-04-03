package com.ecom.controllers;

import com.ecom.models.Products;
import com.ecom.models.User;
import com.ecom.repository.ProductRepository;
import com.ecom.repository.UserRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class ProductController {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserRepository userRepository;

    @ApiOperation(value = "search the products")
    @Tag(name="search the products",description = "This api is accessible by any user without creating its profile. " +
            "User can pass name, type, category as value parameter and price as min,max parameter  to search products " +
            "It will give list of products or empty list if no product found for matching criteria. If no field is set it will return all products")
    @ApiResponses( value={
            @ApiResponse(responseCode ="200", description = "products are found"),
            @ApiResponse(responseCode ="404", description = "products are not found")
    }
    )
    @GetMapping("/search")
    public ResponseEntity getProducts(@RequestParam(required = false,defaultValue ="") String value,@RequestParam(required = false,defaultValue ="0") int min,@RequestParam(required = false,defaultValue ="0") int max)
    {

        Set<Products> products=new HashSet<>();

        //return all products if paramater is set
        if(value.trim().equals("") && min==0 && max==0)
        {
            products.addAll(productRepository.findAll());
        }

        //search by name,type and category
        if(!value.trim().equals(""))
        {

            products.addAll(productRepository.findByName(value.toLowerCase()));
        products.addAll(productRepository.findByType(value.toLowerCase()));
        products.addAll(productRepository.findByCategory(value. toLowerCase()));}


        //search by price
        if(min!=0 && max!=0)
            products.addAll(productRepository.findByMinMaxPrice(min, max));

        else if(min!=0)
            products.addAll(productRepository.findByMinPrice(min));

        else if(max!=0)
            products.addAll(productRepository.findByMaxPrice(max));
        //search by price


        if(products.isEmpty())
            return new ResponseEntity<>(products, HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(products, HttpStatus.OK);

    }


    @ApiOperation(value = "add the products")
    @Tag(name="add the products",description = "This api is accessible only by registered user. Registered user has a seller authority.This api will add products for the user.")
    @ApiResponses( value={
            @ApiResponse(responseCode ="201", description = "product is created"),
            @ApiResponse(responseCode ="400", description = "product not created"),
            @ApiResponse(responseCode ="401", description = "unauthorized access")
    }
    )
    @PostMapping("/seller/add-product")
    public ResponseEntity addProduct(@RequestBody Products products)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username=auth.getName();

        User user= userRepository.findByEmail(username);
        products.setUser(user);

        products.setCategory(products.getCategory().toLowerCase());
        products.setDescription(products.getDescription().toLowerCase());
        products.setName(products.getName().toLowerCase());
        products.setType(products.getType().toLowerCase());
        products.setSpecifications(products.getSpecifications().toLowerCase());

        Products product=productRepository.save(products);

        if(product==null)
         return   new ResponseEntity<>(product, HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @ApiOperation(value = "view products")
    @Tag(name="view products",description = "This api is accessible only by registered user.This api will give list of all products added by the logged in user")
    @ApiResponses( value={
            @ApiResponse(responseCode ="200", description = "products are found"),
            @ApiResponse(responseCode ="404", description = "products are not found"),
            @ApiResponse(responseCode ="401", description = "unauthorized access")
    }
    )
    @GetMapping("/seller/view-products")
    public ResponseEntity viewProducts()
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username=auth.getName();

        User user= userRepository.findByEmail(username);

        List<Products> products = productRepository.findByUser(user.getId());

        if(products.isEmpty())
            return new ResponseEntity<>(products, HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @ApiOperation(value = "update the products")
    @Tag(name="update the products",description = "This api is accessible only by registered user.Pass updated information of product with the product id .It will update product if that product is added by the logged in user and return true otherwise return false.")
    @ApiResponses( value={
            @ApiResponse(responseCode ="202", description = "products updated"),
            @ApiResponse(responseCode ="404", description = "products are not found"),
            @ApiResponse(responseCode ="401", description = "unauthorized access")
    }
    )
    @PutMapping("/seller/update-product")
    public ResponseEntity updateProduct(@RequestBody Products newproducts)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username=auth.getName();

        User user= userRepository.findByEmail(username);

        //check if the product belongs to the user or not. If not, return not found status from else block
      Products products= productRepository.findByUserAndProduct(user.getId(),newproducts.getId());

      if(products!=null) {
          newproducts.setUser(user);
          newproducts.setCategory(newproducts.getCategory().toLowerCase());
          newproducts.setDescription(newproducts.getDescription().toLowerCase());
          newproducts.setName(newproducts.getName().toLowerCase());
          newproducts.setType(newproducts.getType().toLowerCase());
          newproducts.setSpecifications(newproducts.getSpecifications().toLowerCase());
          Products updateproduct = productRepository.save(newproducts);

      }
      else
        return  new ResponseEntity<>(products, HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(newproducts, HttpStatus.ACCEPTED);
    }

    @ApiOperation(value = "delete the products")
    @Tag(name="delete the products",description = "This api is accessible only by registered user.Pass the id of the product through parameter.It will delete product if that product is added by the logged in user and return true otherwise return false.")
    @ApiResponses( value={
            @ApiResponse(responseCode ="201", description = "product deleted"),
            @ApiResponse(responseCode ="404", description = "product not found"),
            @ApiResponse(responseCode ="401", description = "unauthorized access")
    }
    )
    @DeleteMapping("/seller/delete-product")
    public ResponseEntity deleteProduct(@RequestParam int id)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username=auth.getName();

        User user= userRepository.findByEmail(username);
        //check if the product belongs to the user or not. If not, return not found status from else block
        Products product= productRepository.findByUserAndProduct(user.getId(),id);

        if(product!=null) {
             productRepository.deleteByUserAndProduct(user.getId(),id);
        }
        else
           return new ResponseEntity<>(product, HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(product, HttpStatus.ACCEPTED);
    }

}
