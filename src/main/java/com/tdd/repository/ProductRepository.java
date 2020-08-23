package com.tdd.repository;

import com.tdd.model.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Integer> {


    Product findProductById(Integer id);
}
