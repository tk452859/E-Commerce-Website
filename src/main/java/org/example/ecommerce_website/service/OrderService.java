package org.example.ecommerce_website.service;

import org.example.ecommerce_website.model.Order;
import org.example.ecommerce_website.model.OrderItem;
import org.example.ecommerce_website.model.Product;
import org.example.ecommerce_website.dto.OrderItemRequest;
import org.example.ecommerce_website.dto.OrderItemResponse;
import org.example.ecommerce_website.dto.OrderRequest;
import org.example.ecommerce_website.dto.OrderResponse;
import org.example.ecommerce_website.repo.OrderRepo;
import org.example.ecommerce_website.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private OrderRepo orderRepo;

    public OrderResponse placeOrder(OrderRequest request) {

        Order order = new Order();
        String orderId = "ORD" + UUID.randomUUID().toString().substring(0,8).toUpperCase();
        order.setOrderId(orderId);
        order.setCustomerName(request.customerNane());
        order.setEmail(request.email());
        order.setStatus("PLACED");
        order.setDate(LocalDate.now());

        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderItemRequest itemReq : request.items()) {

            Product product = productRepo.findById(itemReq.productid());
            if (product == null) {
                throw new RuntimeException("Product not found with ID: " + itemReq.productid());
            }



            // Check stock availability
            if (itemReq.quantity() > product.getStockQuantity()) {
                throw new RuntimeException(
                        "Insufficient stock for product: " + product.getName()
                );
            }

            product.setStockQuantity(product.getStockQuantity() - itemReq.quantity());
            productRepo.save(product);

            OrderItem orderItem = OrderItem.builder()
                    .product(product)
                    .quantity(itemReq.quantity())
                    .toatlprice(product.getPrice().multiply(BigDecimal.valueOf(itemReq.quantity())))
                    .order(order)
                    .build();

            orderItems.add(orderItem);
        }

        order.setItems(orderItems);
        Order savedOrder = orderRepo.save(order);

        // Build response
        List<OrderItemResponse> itemResponses = orderItems.stream()
                .map(item -> new OrderItemResponse(
                        item.getProduct().getName(),
                        item.getQuantity(),
                        item.getToatlprice()
                ))
                .toList();

        return new OrderResponse(
                savedOrder.getOrderId(),
                savedOrder.getCustomerName(),
                savedOrder.getEmail(),
                savedOrder.getStatus(),
                savedOrder.getDate(),
                itemResponses
        );
    }



    public List<OrderResponse> getAllOrderResponses() {

        List<Order> orders = orderRepo.findAll();
        List<OrderResponse> orderResponses = new ArrayList<>();

        for (Order order : orders) {


            List<OrderItemResponse> itemResponses = new ArrayList<>();

            for(OrderItem item : order.getItems()) {
                OrderItemResponse orderItemResponse = new OrderItemResponse(
                        item.getProduct().getName(),
                        item.getQuantity(),
                        item.getToatlprice()
                );
                itemResponses.add(orderItemResponse);

            }
            OrderResponse orderResponse = new OrderResponse(
                    order.getOrderId(),
                    order.getCustomerName(),
                    order.getEmail(),
                    order.getStatus(),
                    order.getDate(),
                    itemResponses
            );
            orderResponses.add(orderResponse);
        }

        return orderResponses;
    }


}