package com.ecommercebootcamp.demo.Model;

import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;

@Entity
@Data
@Table
//siempre en minuscula, excepto en microsoft
public class ShopCart implements Serializable {
    static final long serialVersionUID = 1L;
    @Id
    @OneToOne
    @JoinColumn(name = "idUser", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private  Long  idShopCart;

    @OneToMany
    private ArrayList<Product> products;

    private Float total;

    private Status status;

    public enum Status {
        IN_PROGRESS,
        CANCELLED,
        COMPLETED,
    }

    public ShopCart(){}

    public ShopCart(Long id,  Float total, Status status) {

        this.idShopCart = id;
        this.products = new ArrayList<Product>();
        this.total = total;
        this.status = status;
    }


}
