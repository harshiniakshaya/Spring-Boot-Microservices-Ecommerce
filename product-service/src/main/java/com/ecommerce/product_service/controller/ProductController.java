package com.ecommerce.product_service.controller;

import com.ecommerce.product_service.entity.Product;
import com.ecommerce.product_service.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    //create a product
    @PostMapping
    public Product addProduct(@RequestBody Product product){
        return productRepository.save(product);
    }

    //get all products
    @GetMapping
    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    //get product by id
    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProductByID(@PathVariable Long productId){
        Product product = productRepository.findById(productId).orElseThrow(
                ()->new RuntimeException("Product not found with ID: "+productId)
        );
        return ResponseEntity.ok(product);
    }
}
