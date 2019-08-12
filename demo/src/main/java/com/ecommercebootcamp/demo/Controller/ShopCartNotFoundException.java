package com.ecommercebootcamp.demo.Controller;

class ShopCartNotFoundException extends RuntimeException {

    ShopCartNotFoundException(Long id) {
        super("Could not find User " + id);
    }
}