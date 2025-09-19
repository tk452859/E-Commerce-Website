package org.example.ecommerce_website.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.ecommerce_website.dto.OrderItemResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "orders")
public class Order {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
   @Column(unique = true)
    private  String orderId;
    private String customerName;
    private String email;
    private String status;
    private LocalDate date;
    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL)
    private List<OrderItem> items;
}
