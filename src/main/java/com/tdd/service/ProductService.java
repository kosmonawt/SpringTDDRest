package com.tdd.service;

import com.tdd.model.Product;
import com.tdd.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product findById(Integer id) {
        return productRepository.findProductById(id);
    }

    public List<Product> findAll() {
        return (List<Product>) productRepository.findAll();
    }

    public Product save(Product product) {
        if (product != null) {
            productRepository.save(product);
            return product;
        } else return null;
    }

    public void deleteProductById(Integer id) {

        if (productRepository.findProductById(id) != null) {
            productRepository.delete(productRepository.findProductById(id));

        }

    }

    public Product update(Product product, Integer id) {
        Product productToUpdate = productRepository.findProductById(id);
        if (product != null) {
            return productRepository.save(productToUpdate);
        } else return null;
    }
}
