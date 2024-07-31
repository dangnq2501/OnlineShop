package com.example.ecommerce_backend.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Entity
@Table(name="\"order\"")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level= AccessLevel.PRIVATE)
public class Order {
    @Id
    @Column(name="\"order_id\"")
    Integer id;

    @OneToOne
    @JoinColumn(name = "\"item_id\"")
    Item item;

    @ManyToOne
    @JoinColumn(name = "\"user_id\"")
    User user;



    int quantity;
    Date orderDate = new Date();
    boolean paid;
}

