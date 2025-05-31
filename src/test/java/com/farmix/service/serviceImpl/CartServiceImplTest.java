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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartServiceImplTest {

    @Mock
    private UserService userService;

    @Mock
    private MenuService menuService;

    @Mock
    private com.farmix.repository.CartRepository cartRepository;

    @Mock
    private com.farmix.repository.CartItemRepository cartItemRepository;

    @InjectMocks
    private CartService cartService;

    private com.farmix.entity.User user;
    private com.farmix.entity.Menu menu;
    private com.farmix.entity.Cart cart;
    private com.farmix.request.CartItemReq cartItemReq;
    private String jwt;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);

        menu = new Menu();
        menu.setId(1L);
        menu.setPrice(10.0);

        cart = new Cart();
        cart.setId(1L);
        cart.setCustomer(user);
        cart.setCartItems(new ArrayList<>());

        cartItemReq = new CartItemReq();
        cartItemReq.setFoodId(1L);
        cartItemReq.setQuantity(2);
        cartItemReq.setExtras(new ArrayList<>());

        jwt = "sample-jwt-token";
    }

    @Test
    void addItemToCart_UserNotFound_ThrowsUserNotFoundException() throws Exception {
        // Arrange
        when(userService.findUserByJwtToken(jwt)).thenReturn(null);

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> cartService.addItemToCart(cartItemReq, jwt));
        verify(userService).findUserByJwtToken(jwt);
        verifyNoInteractions(menuService, cartRepository, cartItemRepository);
    }

    @Test
    void addItemToCart_MenuNotFound_ThrowsCartItemNotFoundException() throws Exception {
        // Arrange
        when(userService.findUserByJwtToken(jwt)).thenReturn(user);
        when(menuService.getFoodById(cartItemReq.getFoodId())).thenReturn(null);

        // Act & Assert
        assertThrows(CartItemNofFoundException.class, () -> cartService.addItemToCart(cartItemReq, jwt));
        verify(userService).findUserByJwtToken(jwt);
        verify(menuService).getFoodById(cartItemReq.getFoodId());
        verifyNoInteractions(cartRepository, cartItemRepository);
    }

    @Test
    void addItemToCart_CartNotFound_ThrowsCartNotFoundException() throws Exception {
        // Arrange
        when(userService.findUserByJwtToken(jwt)).thenReturn(user);
        when(menuService.getFoodById(cartItemReq.getFoodId())).thenReturn(menu);
        when(cartRepository.findByCustomerId(user.getId())).thenReturn(null);

        // Act & Assert
        assertThrows(CartNotFoundException.class, () -> cartService.addItemToCart(cartItemReq, jwt));
        verify(userService).findUserByJwtToken(jwt);
        verify(menuService).getFoodById(cartItemReq.getFoodId());
        verify(cartRepository).findByCustomerId(user.getId());
        verifyNoInteractions(cartItemRepository);
    }

    @Test
    void addItemToCart_ExistingCartItem_UpdatesQuantity() throws Exception {
        // Arrange
        CartItem existingCartItem = new CartItem();
        existingCartItem.setId(1L);
        existingCartItem.setFood(menu);
        existingCartItem.setQuantity(1);
        existingCartItem.setCart(cart);
        cart.getCartItems().add(existingCartItem);

        when(userService.findUserByJwtToken(jwt)).thenReturn(user);
        when(menuService.getFoodById(cartItemReq.getFoodId())).thenReturn(menu);
        when(cartRepository.findByCustomerId(user.getId())).thenReturn(cart);
        when(cartService.updateCartItem(existingCartItem.getId(), existingCartItem.getQuantity() + cartItemReq.getQuantity()))
                .thenReturn(existingCartItem);

        // Act
        CartItem result = cartService.addItemToCart(cartItemReq, jwt);

        // Assert
        assertNotNull(result);
        assertEquals(existingCartItem, result);
        verify(userService).findUserByJwtToken(jwt);
        verify(menuService).getFoodById(cartItemReq.getFoodId());
        verify(cartRepository).findByCustomerId(user.getId());
        verify(cartService).updateCartItem(existingCartItem.getId(), existingCartItem.getQuantity() + cartItemReq.getQuantity());
        verifyNoInteractions(cartItemRepository);
    }

    @Test
    void addItemToCart_NewCartItem_SuccessfullyAdded() throws Exception {
        // Arrange
        when(userService.findUserByJwtToken(jwt)).thenReturn(user);
        when(menuService.getFoodById(cartItemReq.getFoodId())).thenReturn(menu);
        when(cartRepository.findByCustomerId(user.getId())).thenReturn(cart);
        when(cartItemRepository.save(any(CartItem.class))).thenAnswer(invocation -> {
            CartItem savedItem = invocation.getArgument(0);
            savedItem.setId(1L);
            return savedItem;
        });

        // Act
        CartItem result = cartService.addItemToCart(cartItemReq, jwt);

        // Assert
        assertNotNull(result);
        assertEquals(cart, result.getCart());
        assertEquals(menu, result.getFood());
        assertEquals(cartItemReq.getQuantity(), result.getQuantity());
        assertEquals(cartItemReq.getExtras(), result.getExtras());
        assertEquals(cartItemReq.getQuantity() * menu.getPrice(), result.getTotalPrice());
        assertTrue(cart.getCartItems().contains(result));

        verify(userService).findUserByJwtToken(jwt);
        verify(menuService).getFoodById(cartItemReq.getFoodId());
        verify(cartRepository).findByCustomerId(user.getId());
        verify(cartItemRepository).save(any(CartItem.class));
    }
}