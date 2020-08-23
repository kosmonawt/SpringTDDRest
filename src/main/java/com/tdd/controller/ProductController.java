package com.tdd.controller;


import com.tdd.model.Product;
import com.tdd.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")

public class ProductController {

    final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Integer id) {
        return ResponseEntity.ok().body(productService.findById(id));
    }

    @GetMapping()
    public ResponseEntity<?> getProducts() {
        return ResponseEntity.ok().body(productService.findAll());
    }

    @PostMapping
    public Product addProduct(@RequestBody Product product) {
        return productService.save(product);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Integer id) {
        productService.deleteProductById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProductById(@PathVariable Integer id, @RequestBody Product product) {
        Product productToSave = productService.update(product, id);
        if (productToSave != null) {
            return ResponseEntity.ok().body(productToSave);
        } else return null;
    }


}
