package com.ecom.repository;

import com.ecom.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User,Integer> {

    @Query(value = "from User u where u.email=:email")
    public User findByEmail(@Param("email") String email);
}
