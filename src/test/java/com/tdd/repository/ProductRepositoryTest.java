package com.tdd.repository;

import ch.qos.logback.classic.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tdd.model.Product;
import jdk.jfr.Description;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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

    @BeforeEach
    public void setUp() throws IOException {

        File file = Paths.get("src", "test", "resources", "products.json").toFile();

        Product[] products = new ObjectMapper().readValue(file, Product[].class);

        Arrays.stream(products).forEach(productRepository::save);

    }


    @Test
    @DisplayName("Test product not found with non existing ID")
    public void testProductNotFoundWithNonExistingID() {

        Product obtainProduct = productRepository.findProductById(1000);

        Assertions.assertNull(obtainProduct);
    }

    @Test
    @Description("Test product added successfully")
    private void testProductAddedSuccessfully() {

        Product expectedProduct = new Product("Name", "Opis", 1);
        Product savedProduct = productRepository.save(expectedProduct);

        Assertions.assertNotNull(savedProduct, "Product should be saved");
        Assertions.assertNotNull(savedProduct.getId());
        Assertions.assertEquals(expectedProduct.getName(), savedProduct.getName());

    }


    @Test
    @DisplayName("Test update product")
    public void testUpdateProduct() {

        Product updatedProduct = new Product(1, "Updated product", "Opis1", 1, 1);

        Product productToUpdate = productRepository.save(updatedProduct);

        Assertions.assertEquals(productToUpdate.getName(), updatedProduct.getName());

    }

}
