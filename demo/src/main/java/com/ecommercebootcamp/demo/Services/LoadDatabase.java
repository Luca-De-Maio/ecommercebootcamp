package com.ecommercebootcamp.demo.Services;

import com.ecommercebootcamp.demo.Model.ShopCart;
import com.ecommercebootcamp.demo.Model.User;
import com.ecommercebootcamp.demo.Repository.ShopCartRepository;
import com.ecommercebootcamp.demo.Repository.UserRepository;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.ecommercebootcamp.demo.Model.ShopCart.Status.COMPLETED;
import static com.ecommercebootcamp.demo.Model.ShopCart.Status.IN_PROGRESS;
import static com.ecommercebootcamp.demo.Model.ShopCart.Status.CANCELLED;


@Configuration
@Slf4j
class LoadDatabase {

    @Bean
    CommandLineRunner initDatabase2(UserRepository repository) {
        return args -> {
            ShopCart shopCart = new ShopCart();
            ShopCart shopCart1 = new ShopCart();
            log.info("Preloading " + repository.save(new User("BilboBaggins", "burglar" , shopCart)));
            log.info("Preloading " + repository.save(new User("FrodoBaggins", "thief", shopCart1)));

        };
    }
    @Bean
    CommandLineRunner initDatabase1(ShopCartRepository repository) {
        return args -> {
            repository.save(new ShopCart(1L,1.92F,COMPLETED));
            repository.save(new ShopCart(2L,62.02F, CANCELLED)) ;
            repository.findAll().forEach(order -> {
                log.info("Preloaded " + order);
            });

        };
    }

}