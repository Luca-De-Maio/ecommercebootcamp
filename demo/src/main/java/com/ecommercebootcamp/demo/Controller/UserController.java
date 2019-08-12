package com.ecommercebootcamp.demo.Controller;


import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import com.ecommercebootcamp.demo.Model.ShopCart;
import com.ecommercebootcamp.demo.Model.User;
import com.ecommercebootcamp.demo.Repository.ShopCartRepository;
import com.ecommercebootcamp.demo.Repository.UserRepository;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.http.ResponseEntity.created;

@RestController
class UserController {

    private final UserRepository repository;
    private final ShopCartRepository sprepository;
    private final UserResourceAssembler assembler;
    private final ShopCartResourceAssembler assembler1;


    UserController(UserRepository repository,
                   UserResourceAssembler assembler,
                   ShopCartResourceAssembler assembler1,
                   ShopCartRepository sprepository) {

        this.sprepository = sprepository;
        this.repository = repository;
        this.assembler = assembler;
        this.assembler1 = assembler1;
    }

    // Aggregate root

    @GetMapping(value = "/users", produces = "application/json; charset=UTF-8")
    Resources<Resource<User>> all(){
        List<Resource<User>> users = repository.findAll().stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());
        return new Resources<>(users,
                linkTo(methodOn(UserController.class).all()).withSelfRel());
    }

    @PostMapping("/users")
    ResponseEntity<?> newUser(@RequestBody User newUser) throws URISyntaxException {

        Resource<User> resource = assembler.toResource(repository.save(newUser));

        return created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }

    // Single item

    @GetMapping(value = "/users/{id}", produces = "application/json; charset=UTF-8")
    Resource<User> one(@PathVariable Long id) {

        User user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        return assembler.toResource(user);
    }

    @PutMapping("/users/{id}")
     ResponseEntity<?> replaceUser(@RequestBody User newUser, @PathVariable Long id) throws URISyntaxException{
        User updateUser = repository.findById(id)
                .map(user -> {
                    user.setUserName(newUser.getUserName());
                    user.setPassword(newUser.getPassword());
                    user.setShopCart(newUser.getShopCart());
                    return repository.save(user);
                })
                .orElseGet(() -> {
                    newUser.setIdUser(id);
                    return repository.save(newUser);
                });
        Resource<User> resource = assembler.toResource(updateUser);

        return ResponseEntity
                .created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }

    @PostMapping("/users/{id}/shopcart/")
    //aca creo un shop cart en blanco para el usuario id
    public ResponseEntity<?> buildCart(@RequestBody ShopCart newShopCart)throws URISyntaxException {

        Resource<ShopCart> resource = assembler1.toResource(sprepository.save(newShopCart));

        return ResponseEntity
                .created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }

    @PutMapping("/users/shopcart/{id}")
    ResponseEntity<?> replaceShopCart(@RequestBody ShopCart newShopCart, @PathVariable Long id) throws URISyntaxException{
        ShopCart updateShopCart = sprepository.findById(id)
                .map(shopCart -> {
                    shopCart.setTotal(newShopCart.getTotal());
                    shopCart.setStatus(newShopCart.getStatus());
                    shopCart.setProducts(newShopCart.getProducts());
                    return sprepository.save(shopCart);
                })
                .orElseGet(() -> {
                    newShopCart.setIdShopCart(id);
                    return sprepository.save(newShopCart);
                });
        Resource<ShopCart> resource = assembler1.toResource(updateShopCart);

        return ResponseEntity
                .created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }

    @DeleteMapping("/users/{id}")
    ResponseEntity<?> deleteUser(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}