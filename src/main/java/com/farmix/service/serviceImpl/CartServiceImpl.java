package com.farmix.service.serviceImpl;

import com.farmix.entity.Cart;
import com.farmix.entity.CartItem;
import com.farmix.entity.Food;
import com.farmix.entity.User;
import com.farmix.repository.CartItemRepository;
import com.farmix.repository.CartRepository;
import com.farmix.request.CartItemReq;
import com.farmix.service.CartService;
import com.farmix.service.FoodService;
import com.farmix.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private FoodService foodService;

    @Override
    public CartItem addItemToCart(CartItemReq req, String jwt) throws Exception {

        User user = userService.findUserByJwtToken(jwt);

        Food food = foodService.getFoodById(req.getFoodId());

        Cart cart = cartRepository.findByCustomerId(user.getId());

        for(CartItem cartItem : cart.getCartItems()) {
            if (cartItem.getFood().equals(food)){
                int newQuantity = cartItem.getQuantity()+req.getQuantity();
                return updateCartItem(cartItem.getId(), newQuantity);
            }
        }

        CartItem newCartItem = new CartItem();
        newCartItem.setCart(cart);
        newCartItem.setFood(food);
        newCartItem.setQuantity(req.getQuantity());
        newCartItem.setExtras(req.getExtras());
        newCartItem.setTotalPrice(req.getQuantity()*food.getPrice());

        CartItem cartItem = cartItemRepository.save(newCartItem);

        cart.getCartItems().add(cartItem);

        return cartItem;
    }

    @Override
    public CartItem updateCartItem(Long cartItemId, int quantity) throws Exception {

        Optional<CartItem> cartItemOptional = cartItemRepository.findById(cartItemId);

        if (cartItemOptional.isEmpty()){
            throw new Exception("CartItem not found");
        }
        CartItem cartItem = cartItemOptional.get();
        cartItem.setQuantity(quantity);
        cartItem.setTotalPrice(quantity*cartItem.getFood().getPrice());

        return cartItemRepository.save(cartItem);
    }

    @Override
    public Cart removeCartItem(Long cartItemId, String jwt) throws Exception {

        User user = userService.findUserByJwtToken(jwt);

        Cart cart = cartRepository.findByCustomerId(user.getId());

        Optional<CartItem> cartItemOptional = cartItemRepository.findById(cartItemId);

        if (cartItemOptional.isEmpty()){
            throw new Exception("CartItem not found");
        }

        CartItem cartItem = cartItemOptional.get();
        cart.getCartItems().remove(cartItem);

        return cartRepository.save(cart);
    }

    @Override
    public Long calculateCartTotal(Cart cart) throws Exception {
        long total = 0L;

        for(CartItem cartItem : cart.getCartItems()) {
            total = (long) (cartItem.getFood().getPrice()*cartItem.getQuantity());
        }

        return total;
    }

    @Override
    public Cart findCartById(Long cartId) throws Exception {

        Optional<Cart> cartOptional = cartRepository.findById(cartId);
        if (cartOptional.isEmpty()){
            throw new Exception("CartItem not found");
        }

        return cartOptional.get();
    }

    @Override
    public Cart findCartByUserId(Long userId) throws Exception {
        Cart cart = cartRepository.findByCustomerId(userId);

        cart.setTotal(calculateCartTotal(cart));

        return cart;

    }

    @Override
    public Cart clearCart(Long userId) throws Exception {
        Cart cart = cartRepository.findByCustomerId(userId);

        cart.getCartItems().clear();

        return cartRepository.save(cart);
    }
}
