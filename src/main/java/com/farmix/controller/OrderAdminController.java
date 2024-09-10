package com.farmix.controller;

import com.farmix.entity.Order;
import com.farmix.entity.User;
import com.farmix.request.OrderRequest;
import com.farmix.service.OrderService;
import com.farmix.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class OrderAdminController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @GetMapping("/all-orders/restaurant/{id}")
    public ResponseEntity<List<Order>> getAllOrdersByUserId(@PathVariable Long id,
                                                            @RequestParam(required = false) String status,
                                                            @RequestHeader("Authorization") String jwt
                                                            ) throws Exception {
        User user = userService.findUserByJwtToken(jwt);

        List<Order> orders = orderService.getRestartOrders(id, status);

        return ResponseEntity.ok(orders);
    }

    @PutMapping("/order/{id}/{status}")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable Long id,
                                                   @PathVariable String status,
                                                   @RequestHeader("Authorization") String jwt
                                                   ) throws Exception {
        User user = userService.findUserByJwtToken(jwt);

        Order updatedOrderStatus = orderService.updateOrderStatus(id, status);

        return ResponseEntity.ok(updatedOrderStatus);

    }

}
