package com.ecommercebootcamp.demo.Controller;

class UserNotFoundException extends RuntimeException {

    UserNotFoundException(Long id) {
        super("Could not find User " + id);
    }
}