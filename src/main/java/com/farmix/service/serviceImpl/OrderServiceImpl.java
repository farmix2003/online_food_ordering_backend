package com.farmix.service.serviceImpl;

import com.farmix.entity.*;
import com.farmix.exception.CartItemNofFoundException;
import com.farmix.exception.OrderNotFoundException;
import com.farmix.repository.*;
import com.farmix.request.OrderRequest;
import com.farmix.service.CartService;
import com.farmix.service.OrderService;
import com.farmix.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderedFoodRepository orderedFoodRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private CartService cartService;

    @Override
    public Order createOrder(OrderRequest order, User user) throws Exception {

        Address shippingAddress = addressRepository.save(order.getShippingAddress());

        if(!user.getAddressList().contains(shippingAddress)){
            user.getAddressList().add(shippingAddress);
            userRepository.save(user);
        }

        Restaurant restaurant = restaurantService.getRestaurantById(order.getRestaurantId());
        Cart cart = cartService.findCartByUserId(user.getId());

        if (cart.getCartItems().isEmpty()){
            throw new CartItemNofFoundException("Cart is empty.");
        }


        Order newOrder = new Order();
        newOrder.setRestaurant(restaurant);
        newOrder.setCreatedAt(new Date(new java.util.Date().getTime()));
        newOrder.setOrderStatus("PENDING");
        newOrder.setCustomer(user);
        newOrder.setShippingAddress(shippingAddress);



        List<OrderedFood> orderedFoods = cart.getCartItems().stream()
                .map(cartItem -> {
                    OrderedFood orderedFood = new OrderedFood();
                    orderedFood.setFood(cartItem.getFood());
                    orderedFood.setQuantity(cartItem.getQuantity());
                    orderedFood.setExtras(cartItem.getExtras());
                    orderedFood.setTotalPrice(cartItem.getTotalPrice());
                    return orderedFood;
                        }).collect(Collectors.toList());

        orderedFoodRepository.saveAll(orderedFoods);

        Long totalPrice = cartService.calculateCartTotal(cart);

        newOrder.setTotalPrice(totalPrice);
        newOrder.setOrderedFoodList(orderedFoods);

        Order savedOrder = orderRepository.save(newOrder);
        restaurant.getOrders().add(savedOrder);

        return savedOrder;
    }

    @Override
    public Order updateOrderStatus(Long orderId, String status) throws Exception {
        Order order = getOrderById(orderId);
        if (status.equals("PENDING") ||
                status.equals("COMPLETED") ||
                status.equals("DELIVERED") ||
                status.equals("OUT_FOR_DELIVERY")
        ) {
            order.setOrderStatus(status);
            return orderRepository.save(order);
        }
        throw new Exception("Invalid order status");
    }

    @Override
    public void cancelOrder(Long orderId) throws Exception {
       Order order = getOrderById(orderId);
       if (order == null){
           throw new OrderNotFoundException("Order with id "+orderId+" not found");
       }
       order.setOrderStatus("CANCELED");
       orderRepository.save(order);
    }

    @Override
    public List<Order> getOrdersByUserId(Long userId) throws Exception {

        return orderRepository.findByCustomerId(userId);
    }

    @Override
    public Order getOrderById(Long orderId) throws Exception {
       return orderRepository.findById(orderId)
               .orElseThrow(() -> new OrderNotFoundException("Order wth id "+orderId+" not found"));
    }

    @Override
    public List<Order> getRestartOrders(Long restaurantId, String status) throws Exception {

        List<Order> orders = orderRepository.findByRestaurantId(restaurantId);
        if (status != null && !status.isEmpty()) {
            orders = orders.stream().filter(order ->
                    order.getOrderStatus().equals(status))
                    .collect(Collectors.toList());
        }

        return orders;
    }
}
