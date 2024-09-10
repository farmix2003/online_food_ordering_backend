package com.farmix.service.serviceImpl;

import com.farmix.entity.*;
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

        Address shippingAddress = order.getShippingAddress();

        Address savedAddress = addressRepository.save(shippingAddress);

        if(!user.getAddressList().contains(savedAddress)){
            user.getAddressList().add(savedAddress);
            userRepository.save(user);
        }

        Restaurant restaurant = restaurantService.getRestaurantById(order.getRestaurantId());

        Order newOrder = new Order();
        newOrder.setRestaurant(restaurant);
        newOrder.setCreatedAt(new Date(new java.util.Date().getTime()));
        newOrder.setOrderStatus("PENDING");
        newOrder.setCustomer(user);
        newOrder.setShippingAddress(savedAddress);

        Cart cart = cartService.findCartByUserId(user.getId());

        List<OrderedFood> orderedFoods = new ArrayList<>();

        for (CartItem cartItem : cart.getCartItems()) {
            OrderedFood orderedFood = new OrderedFood();

            orderedFood.setQuantity(cartItem.getQuantity());
            orderedFood.setFood(cartItem.getFood());
            orderedFood.setTotalPrice(cartItem.getTotalPrice());
            orderedFood.setIngredients(cartItem.getExtras());

            OrderedFood orderedFood1 = orderedFoodRepository.save(orderedFood);
            orderedFoods.add(orderedFood1);
        }

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

    }

    @Override
    public List<Order> getOrdersByUserId(Long userId) throws Exception {

        return orderRepository.findByCustomerId(userId);
    }

    @Override
    public Order getOrderById(Long orderId) throws Exception {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        return optionalOrder.orElse(null);
    }

    @Override
    public List<Order> getRestartOrders(Long restaurantId, String status) throws Exception {

        List<Order> orders = orderRepository.findByRestaurantId(restaurantId);
        if (status != null) {
            orders.stream().filter(order ->
                    order.getOrderStatus()
                            .equals(status)).collect(Collectors.toList());
        }

        return orders;
    }
}
