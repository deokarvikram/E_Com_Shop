package com.ecom.controllers;

import com.ecom.models.ProductSearch;
import com.ecom.models.Products;
import com.ecom.models.User;
import com.ecom.repository.ProductRepository;
import com.ecom.repository.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

@RestController
public class ProductController {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserRepository userRepository;

    @ApiOperation(value = "search the products")
    @Tag(name="search the products",description = "This api is accessible by any user without creating its profile. " +
            "User can set value to any field or all fields to search products. Fields are name, type, category, minprice and maxprice. " +
            "It will give list of products or empty list if no product found for matching criteria. If no field is set it will return all products")
    @GetMapping("/search")
    public List<Products> getProducts(@RequestBody ProductSearch data)
    {
        List<Products> products=new ArrayList<>();


        if(!data.getName().equals(""))
        products.addAll(productRepository.findByName(data.getName().toLowerCase()));

        if(!data.getType().equals(""))
        products.addAll(productRepository.findByType(data.getType().toLowerCase()));

        if(!data.getCategory().equals(""))
        products.addAll(productRepository.findByCategory(data.getCategory().toLowerCase()));

        if(data.getMinPrice()!=0 && data.getMaxPrice()!=0)
            products.addAll(productRepository.findByMinMaxPrice(data.getMinPrice(), data.getMaxPrice()));

         else if(data.getMinPrice()!=0)
        products.addAll(productRepository.findByMinPrice(data.getMinPrice()));

        else if(data.getMaxPrice()!=0)
        products.addAll(productRepository.findByMaxPrice(data.getMaxPrice()));

        if(products.size()==0)
            products.addAll(productRepository.findAll());


        return products;
    }


    @ApiOperation(value = "add the products")
    @Tag(name="add the products",description = "This api is accessible only by registered authorized user. Registered user is a seller.This api will add products for the user.")
    @PostMapping("/seller/add-product")
    public boolean addProduct(@RequestBody Products products)
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
                return false;
            return true;
    }

    @ApiOperation(value = "view products")
    @Tag(name="view products",description = "This api is accessible only by registered authorized user.This api will give list of all products added by the logged in user")
    @GetMapping("/seller/view-products")
    public List<Products> viewProducts()
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username=auth.getName();

        User user= userRepository.findByEmail(username);

        return productRepository.findByUser(user.getId());
    }

    @ApiOperation(value = "update the products")
    @Tag(name="update the products",description = "This api is accessible only by registered authorized user.Pass updated information of product with the product id .It will update product if that product is added by the logged in user and return true otherwise return false.")
    @PutMapping("/seller/update-product")
    public boolean updateProduct(@RequestBody Products newproducts)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username=auth.getName();

        User user= userRepository.findByEmail(username);

      Products products= productRepository.findByUserAndProduct(user.getId(),newproducts.getId());

      if(products!=null) {
          newproducts.setUser(user);
          newproducts.setCategory(newproducts.getCategory().toLowerCase());
          newproducts.setDescription(newproducts.getDescription().toLowerCase());
          newproducts.setName(newproducts.getName().toLowerCase());
          newproducts.setType(newproducts.getType().toLowerCase());
          newproducts.setSpecifications(newproducts.getSpecifications().toLowerCase());
          Products updateproduct = productRepository.save(newproducts);
          if (updateproduct == null)
              return false;
      }
      else
         return false;

        return true;
    }

    @ApiOperation(value = "delete the products")
    @Tag(name="delete the products",description = "This api is accessible only by registered authorized user.Pass the id of the product through parameter.It will delete product if that product is added by the logged in user and return true otherwise return false.")
    @DeleteMapping("/seller/delete-product")
    public boolean deleteProduct(@RequestParam int id)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username=auth.getName();

        User user= userRepository.findByEmail(username);

        Products product= productRepository.findByUserAndProduct(user.getId(),id);

        if(product!=null) {
             productRepository.deleteByUserAndProduct(user.getId(),id);
        }
        else
        {
            return false;
        }

        return true;
    }

}
