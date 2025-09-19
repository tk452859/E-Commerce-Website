package org.example.ecommerce_website.controller;


import jakarta.persistence.Entity;
import org.example.ecommerce_website.dto.OrderRequest;
import org.example.ecommerce_website.dto.OrderResponse;
import org.example.ecommerce_website.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api")
@CrossOrigin("http://localhost:5173")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/orders/place")
    public ResponseEntity<OrderResponse> placeOrder(@RequestBody OrderRequest orderRequest) {
                       OrderResponse orderResponse = orderService.placeOrder(orderRequest);
                       return ResponseEntity.ok().body(orderResponse);
    }
    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponse>> getAllOrdersResponses() {
        List<OrderResponse> orderResponses= orderService.getAllOrderResponses();
        return ResponseEntity.ok().body(orderResponses);
    }
}
