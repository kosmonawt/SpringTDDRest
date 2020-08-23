package com.tdd.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tdd.model.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;


@ExtendWith({SpringExtension.class})
@SpringBootTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;


    @Test
    @DisplayName("Test product not found with non existing ID")
    public void testProductNotFoundWithNonExistingID() throws IOException {

        File file = Paths.get("src", "test", "resources", "products.json").toFile();

        Product[] products = new ObjectMapper().readValue(file, Product[].class);

        Arrays.stream(products).forEach(productRepository::save);

        Product obtainProduct = productRepository.findProductById(1000);

        Assertions.assertNull(obtainProduct);
    }


}
