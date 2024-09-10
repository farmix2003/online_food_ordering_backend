package com.farmix.service;

import com.farmix.entity.Order;
import com.farmix.entity.User;
import com.farmix.request.OrderRequest;

import java.util.List;

public interface OrderService {

     Order createOrder(OrderRequest order, User user) throws Exception;
     Order updateOrderStatus(Long orderId, String status) throws Exception;
     void cancelOrder(Long orderId) throws Exception;
     List<Order> getOrdersByUserId(Long userId) throws Exception;
     Order getOrderById(Long orderId) throws Exception;
     List<Order> getRestartOrders(Long restaurantId, String status) throws Exception;

}
