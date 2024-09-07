package com.farmix.service;

import com.farmix.entity.Cart;
import com.farmix.entity.CartItem;
import com.farmix.request.CartItemReq;

public interface CartService {

    CartItem addItemToCart(CartItemReq cartItem, String jwt) throws Exception;
    CartItem updateCartItem(Long cartItemId, int quantity) throws Exception;
    Cart removeCartItem(Long cartItemId, String jwt) throws Exception;
    Long calculateCartTotal(Cart cart) throws Exception;
    Cart findCartById(Long cartId) throws Exception;
    Cart findCartByUserId(Long userId) throws Exception;
    Cart clearCart(Long userId) throws Exception;
}
