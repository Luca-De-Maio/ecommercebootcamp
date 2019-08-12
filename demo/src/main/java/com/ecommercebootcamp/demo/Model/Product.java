package com.ecommercebootcamp.demo.Model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Product {
    @ManyToOne
    @JoinColumn(name= "idShopCart")
    private @Id @GeneratedValue Long idProduct;


    private String name;


    private Double price;


    private Integer stock;


    private String category;


}
