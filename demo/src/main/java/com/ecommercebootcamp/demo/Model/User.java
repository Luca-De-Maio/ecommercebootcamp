package com.ecommercebootcamp.demo.Model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
//anotacion lombok para crear todos los getters y setters,
@Entity
//JPA anotation para hacer este objeto listo para guardar en una jpa based data store
@Table(name="user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @OneToOne
    private Long idUser;


    @NotNull
    @Column(unique = true)
    private String userName;


    @NotNull
    @JsonIgnore
    private String password;

    @NotNull
    @OneToOne
    @JoinColumn()
    private ShopCart shopCart;
    //actualizar nick name por que no tengo nombre de user solo nombre

    public User(){}

    public User(String name, String password, ShopCart shopcart) {
        this.userName = name;
        this.password = password;
        this.shopCart = shopcart;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }
}