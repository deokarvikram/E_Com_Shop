package com.ecom.repository;

import com.ecom.models.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

public interface ProductRepository extends JpaRepository<Products,Integer> {

    @Query(value = "select * from Products p where p.user_id=:id", nativeQuery = true)
    public List<Products> findByUser(@Param("id") int id);

    @Query(value = "select * from Products p where p.user_id=:uid and p.id=:pid", nativeQuery = true)
    public Products findByUserAndProduct(int uid,int pid);

    @Query(value = "delete from Products p where p.user_id=:uid and p.id=:pid", nativeQuery = true)
    @Modifying
    @Transactional
    public void deleteByUserAndProduct(int uid,int pid);

    @Query(value = "select * from Products p where lower(p.name) like %:name%" , nativeQuery = true)
    public List<Products> findByName(String name);

    @Query(value = "select * from Products p where p.type like %:type%" , nativeQuery = true)
    public List<Products> findByType(String type);

    @Query(value = "select * from Products p where p.category like %:category%" , nativeQuery = true)
    public List<Products> findByCategory(String category);

    @Query(value = "select * from Products p where p.price>=:price" , nativeQuery = true)
    public List<Products> findByMinPrice(@Param("price") int price);

    @Query(value = "select * from Products p where p.price<=:price", nativeQuery = true)
    public List<Products> findByMaxPrice(@Param("price") int price);

    @Query(value = "select * from Products p where p.price between :min  and :max", nativeQuery = true)
    public List<Products> findByMinMaxPrice(@Param("min") int min,@Param("max") int max);


}
