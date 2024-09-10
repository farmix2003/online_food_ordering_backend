package com.farmix.controller;


import com.farmix.entity.Order;
import com.farmix.entity.User;
import com.farmix.request.OrderRequest;
import com.farmix.response.MessageResponse;
import com.farmix.service.OrderService;
import com.farmix.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @PostMapping("/order")
    public ResponseEntity<Order> createOrder(@RequestBody OrderRequest req,
                                             @RequestHeader("Authorization") String jwt
                                             ) throws Exception {
        User user = userService.findUserByJwtToken(jwt);

        Order createdOrder = orderService.createOrder(req, user);

        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }

    @GetMapping("/all-orders")
    public ResponseEntity<List<Order>> getAllOrdersByUserId(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);

        List<Order> orders = orderService.getOrdersByUserId(user.getId());

        return ResponseEntity.ok(orders);
    }

    @PutMapping("/order/cancel/{id}")
    public ResponseEntity<MessageResponse> cancelOrder (@PathVariable Long id) throws Exception {

         orderService.cancelOrder(id);

         MessageResponse msg = new MessageResponse();
         msg.setMessage("Order cancelled");

         return ResponseEntity.ok(msg);
    }

}
