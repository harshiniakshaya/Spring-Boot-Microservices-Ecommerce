package com.ecommerce.order_service.controller;

import com.ecommerce.order_service.dto.OrderResponseDTO;
import com.ecommerce.order_service.dto.ProductDTO;
import com.ecommerce.order_service.entity.Order;
import com.ecommerce.order_service.repository.OrderRepository;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private WebClient.Builder webClientBuilder;

    //create a method to place order
    @PostMapping("/placeorder")
    public Mono<ResponseEntity<OrderResponseDTO>> placeOrder(@RequestBody Order order){
        //fetch product details from product service
        return  webClientBuilder.build().get().uri("http://localhost:8081/products/"+order.getProductId()).retrieve()
                .bodyToMono(ProductDTO.class).map(productDTO -> {
                   OrderResponseDTO responseDTO = new OrderResponseDTO();
                   responseDTO.setProductId(order.getProductId());
                   responseDTO.setQuantity(order.getQuantity());
                   //set product details
                   responseDTO.setProductName(productDTO.getName());
                   responseDTO.setProductPrice(productDTO.getPrice());
                   responseDTO.setTotalPrice(order.getQuantity() * productDTO.getPrice());

                   //save order details to DB
                    orderRepository.save(order);
                    responseDTO.setOrderId(order.getId());
                    return ResponseEntity.ok(responseDTO);
                });
    }

    //get all orders
    @GetMapping
    public List<Order> getAllOrders(){
        return orderRepository.findAll();
    }
}
