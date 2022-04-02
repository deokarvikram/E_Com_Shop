package com.ecom.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User //implements UserDetails
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "first_name",nullable = false)
    private String firstName;
    @Column(name = "middle_name")
    private String middleName;
    @Column(name = "last_name",nullable = false)
    private String lastName;

    private String email;
    @Column(unique = true,nullable = false)
    private String phone;
    @Column(name = "alternative_phone")
    private String alternativePhone;
    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "user")
    private List<Products> products;

    @JsonIgnore
    private String authority;
}
