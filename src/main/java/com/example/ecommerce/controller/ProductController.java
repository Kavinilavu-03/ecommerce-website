package com.example.ecommerce.controller;

import com.example.ecommerce.model.CartItem;

import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.ProductRepository;

import org.springframework.web.bind.annotation.*;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {

    private final ProductRepository repo;
    private List<CartItem> cart = new ArrayList<>();

    public ProductController(ProductRepository repo) {
        this.repo = repo;
    }

    @PostConstruct
    public void loadData() {
        repo.save(new Product("Charger", 500, "/images/charger.jpeg"));
        repo.save(new Product("Earphones", 2000, "/images/earphones.jpeg"));
        repo.save(new Product("Mobilecase", 1000, "/images/mobilecase.jpeg"));
    }

    @GetMapping("/products")
    public List<Product> getProducts() { return repo.findAll(); }

    @PostMapping("/cart")
    public String addToCart(@RequestBody CartItem item) {
        cart.add(item);
        return "Item added successfully!";
    }

    @GetMapping("/cart")
    public List<CartItem> getCart() { return cart; }

    @GetMapping("/checkout")
    public String checkout() {
        List<Product> products = repo.findAll();
        double total = cart.stream()
                .mapToDouble(item -> {
                    Product p = products.stream()
                            .filter(pr -> pr.getId().equals(item.getProductId()))
                            .findFirst().orElse(null);

                    return p != null ? p.getPrice() * item.getQuantity() : 0;
                }).sum();
        return "Total Amount: ₹" + total;
    }
}