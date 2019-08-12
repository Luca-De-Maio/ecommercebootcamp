package com.ecommercebootcamp.demo.Controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import com.ecommercebootcamp.demo.Model.ShopCart;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

@Component
class ShopCartResourceAssembler implements ResourceAssembler<ShopCart, Resource<ShopCart>> {

    @Override
    public Resource<ShopCart> toResource(ShopCart sp) {

        // Unconditional links to single-item resource and aggregate root

        Resource<ShopCart> shopCartResource = new Resource<>(sp,
                linkTo(methodOn(ShopCartController.class).one(sp.getIdShopCart())).withSelfRel(),
                linkTo(methodOn(ShopCartController.class).all()).withRel("shopcarts")
        );

        // Conditional links based on state of the order

        if (sp.getStatus() == ShopCart.Status.IN_PROGRESS) {
            shopCartResource.add(
                    linkTo(methodOn(ShopCartController.class)
                            .cancel(sp.getIdShopCart())).withRel("cancel"));
            shopCartResource.add(
                    linkTo(methodOn(ShopCartController.class)
                            .complete(sp.getIdShopCart())).withRel("complete"));
        }

        return shopCartResource;
    }
}