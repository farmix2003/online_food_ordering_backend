package com.farmix.controller;

import com.farmix.entity.Cart;
import com.farmix.entity.CartItem;
import com.farmix.entity.User;
import com.farmix.request.CartItemReq;
import com.farmix.request.UpdateCartItemReq;
import com.farmix.service.CartService;
import com.farmix.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    UserService userService;

    @PostMapping("/cart/add")
    public ResponseEntity<CartItem> addItemToCart(@RequestBody CartItemReq cartItem,
                                                  @RequestHeader("Authorization") String jwt
                                                  ) throws Exception {
        CartItem cartItem1 = cartService.addItemToCart(cartItem, jwt);

        return ResponseEntity.ok(cartItem1);
    }



    @PutMapping("/cart-item/update")
    public ResponseEntity<CartItem> updateCartItemQuantity(@RequestBody UpdateCartItemReq req,
                                                           @RequestHeader("Authorization") String jwt
                                                           ) throws Exception {
      CartItem cartItem = cartService.updateCartItem(req.getId(), req.getQuantity());
      return ResponseEntity.ok(cartItem);
    }


    @DeleteMapping("/cart-item/delete/{id}")
    public ResponseEntity<Cart> removeItemFromCart(@PathVariable Long id,
                                                   @RequestHeader("Authorization") String jwt
                                                   ) throws Exception {
    Cart cartItem = cartService.removeCartItem(id, jwt);

    return ResponseEntity.ok(cartItem);
    }

    @PutMapping("/cart/clear")
    public ResponseEntity<Cart> clearCart(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);

        Cart cart = cartService.clearCart(user.getId());

        return ResponseEntity.ok(cart);
    }


    @GetMapping("/cart")
    public ResponseEntity<Cart> findCartByUserId(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);

        Cart cart = cartService.findCartByUserId(user.getId());

        return ResponseEntity.ok(cart);

    }


}
