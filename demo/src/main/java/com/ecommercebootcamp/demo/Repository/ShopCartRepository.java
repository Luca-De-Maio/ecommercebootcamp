package com.ecommercebootcamp.demo.Repository;

import com.ecommercebootcamp.demo.Model.ShopCart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopCartRepository extends JpaRepository<ShopCart, Long> {


}
