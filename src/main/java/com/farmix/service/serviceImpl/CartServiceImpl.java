package com.farmix.service.serviceImpl;

import com.farmix.entity.Cart;
import com.farmix.entity.CartItem;
import com.farmix.entity.Menu;
import com.farmix.entity.User;
import com.farmix.exception.CartItemNofFoundException;
import com.farmix.exception.CartNotFoundException;
import com.farmix.exception.UserNotFoundException;
import com.farmix.repository.CartItemRepository;
import com.farmix.repository.CartRepository;
import com.farmix.request.CartItemReq;
import com.farmix.service.CartService;
import com.farmix.service.MenuService;
import com.farmix.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private MenuService menuService;

    @Override
    @Transactional
    public CartItem addItemToCart(CartItemReq req, String jwt) throws Exception {

        User user = userService.findUserByJwtToken(jwt);
        if(user == null){
            throw new UserNotFoundException("User not found for the given JWT token");
        }

        Menu menu = menuService.getFoodById(req.getFoodId());
        if (menu == null){
            throw new CartItemNofFoundException("Food Menu not found!");
        }

        Cart cart = cartRepository.findByCustomerId(user.getId());
        if (cart == null){
            throw new CartNotFoundException("Cart not found for the user");
        }

        Optional<CartItem> existingCartItem = cart.getCartItems().stream()
                .filter(cartItem -> cartItem.getFood().equals(menu))
                .findFirst();

        if (existingCartItem.isPresent()) {
            CartItem cartItem = existingCartItem.get();
            return updateCartItem(cartItem.getId(), cartItem.getQuantity() + req.getQuantity());
        }

        CartItem newCartItem = new CartItem();
        newCartItem.setCart(cart);
        newCartItem.setFood(menu);
        newCartItem.setQuantity(req.getQuantity());
        newCartItem.setExtras(req.getExtras());
        newCartItem.setTotalPrice(req.getQuantity()* menu.getPrice());

        CartItem cartItem = cartItemRepository.save(newCartItem);

        cart.getCartItems().add(cartItem);

        return cartItem;
    }

    @Override
    @Transactional
    public CartItem updateCartItem(Long cartItemId, int quantity) throws Exception {

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartItemNofFoundException("CartItem not found with ID: " + cartItemId));

        cartItem.setQuantity(quantity);
        cartItem.setTotalPrice(quantity*cartItem.getFood().getPrice());

        return cartItemRepository.save(cartItem);
    }

    @Override
    @Transactional
    public Cart removeCartItem(Long cartItemId, String jwt) throws Exception {

        User user = userService.findUserByJwtToken(jwt);
        if (user == null) {
            throw new UserNotFoundException("User not found for the given JWT token");
        }

        Cart cart = cartRepository.findByCustomerId(user.getId());
        if (cart == null) {
            throw new CartNotFoundException("Cart not found for the user");
        }

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartItemNofFoundException("CartItem not found with ID: " + cartItemId));

        cart.getCartItems().remove(cartItem);
        cartItemRepository.delete(cartItem);

        return cartRepository.save(cart);
    }

    @Override
    public Long calculateCartTotal(Cart cart) throws Exception {
        if (cart == null || cart.getCartItems().isEmpty()) {
            return 0L;
        }

        return cart.getCartItems().stream()
                .mapToLong(item -> (long) (item.getFood().getPrice() * item.getQuantity()))
                .sum();
    }

    @Override
    public Cart findCartById(Long cartId) {
        return cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found with ID: " + cartId));
    }

    @Override
    public Cart findCartByUserId(Long userId) throws Exception {
        Cart cart = cartRepository.findByCustomerId(userId);

        cart.setTotal(calculateCartTotal(cart));

        return cart;
    }

    @Override
    @Transactional
    public Cart clearCart(Long userId) {
        Cart cart = cartRepository.findByCustomerId(userId);
        if (cart == null) {
            throw new CartNotFoundException("Cart not found for the user");
        }

        cart.getCartItems().clear();
        return cartRepository.save(cart);
    }
}
