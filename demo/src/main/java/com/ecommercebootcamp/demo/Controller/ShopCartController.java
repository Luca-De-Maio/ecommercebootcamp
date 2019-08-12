package com.ecommercebootcamp.demo.Controller;

import com.ecommercebootcamp.demo.Model.ShopCart;
import com.ecommercebootcamp.demo.Repository.ShopCartRepository;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.VndErrors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class ShopCartController{

    private final ShopCartRepository shopCartRepository;
    private final ShopCartResourceAssembler assembler;



    ShopCartController(ShopCartRepository shopCartRepository,
                       ShopCartResourceAssembler assembler){
        this.shopCartRepository = shopCartRepository;
        this.assembler = assembler;
    }

    @GetMapping("/shopcarts")
    Resources<Resource<ShopCart>> all() {

        List<Resource<ShopCart>> shopcarts = shopCartRepository.findAll().stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());

        return new Resources<>(shopcarts,
                linkTo(methodOn(ShopCartController.class).all()).withSelfRel());
    }

    @GetMapping("/shopcarts/{id}")
    Resource<ShopCart> one(@PathVariable Long id) {
        return assembler.toResource(
                shopCartRepository.findById(id)
                        .orElseThrow(() -> new ShopCartNotFoundException(id)));
    }

    @PostMapping("/shopcarts")
    public ResponseEntity<Resource<ShopCart>> newShopCart(@RequestBody ShopCart sp ) {

        sp.setStatus(ShopCart.Status.IN_PROGRESS);
        ShopCart newShopCart = shopCartRepository.save(sp);

        return ResponseEntity
                .created(linkTo(methodOn(ShopCartController.class).one(newShopCart.getIdShopCart())).toUri())
                .body(assembler.toResource(newShopCart));
    }
    @DeleteMapping("/shopcarts/{id}/cancel")
    ResponseEntity<ResourceSupport> cancel(@PathVariable Long id) {

        ShopCart order = shopCartRepository.findById(id).orElseThrow(() -> new ShopCartNotFoundException(id));

        if (order.getStatus() == ShopCart.Status.IN_PROGRESS) {
            order.setStatus(ShopCart.Status.CANCELLED);
            return ResponseEntity.ok(assembler.toResource(shopCartRepository.save(order)));
        }

        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(new VndErrors.VndError("Method not allowed", "You can't cancel an order that is in the " + order.getStatus() + " status"));
    }
    @PutMapping("/shopcarts/{id}/complete")
    ResponseEntity<ResourceSupport> complete(@PathVariable Long id) {

        ShopCart shopCart = shopCartRepository.findById(id).orElseThrow(() -> new ShopCartNotFoundException(id));

        if (shopCart.getStatus() == ShopCart.Status.IN_PROGRESS) {
            shopCart.setStatus(ShopCart.Status.COMPLETED);
            return ResponseEntity.ok(assembler.toResource(shopCartRepository.save(shopCart)));
        }

        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(new VndErrors.VndError("Method not allowed", "You can't complete an order that is in the " + shopCart.getStatus() + " status"));
    }
}

