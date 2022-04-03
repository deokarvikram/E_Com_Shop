package com.ecom.service;

import com.ecom.models.Products;
import com.ecom.models.User;
import com.ecom.repository.ProductRepository;
import com.ecom.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserRepository userRepository;

  public Set<Products> getProducts(String value, int min,int max)
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

      return products;
  }

  public Products  addProduct(Products products)
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
      
      return productRepository.save(products);
  }

    public List<Products> viewProducts()
    {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username=auth.getName();

        User user= userRepository.findByEmail(username);

        return productRepository.findByUser(user.getId());
    }
}
