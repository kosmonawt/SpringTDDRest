package com.tdd.service;

import com.tdd.model.Product;
import com.tdd.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Collection;

import static org.mockito.Mockito.doReturn;


@SpringBootTest
@ExtendWith({SpringExtension.class})
class ProductServiceTest {


    @Autowired
    ProductService productService;
    @MockBean
    ProductRepository productRepository;


    @Test
    @DisplayName("Find product with id successfully")
    void testFindProductById() {
        // Given
        Product mockProduct = new Product(1, "Nazwa", "Opis", 1, 1);
        doReturn(mockProduct).when(productRepository).findProductById(1);
        // When
        Product receivedProduct = productService.findById(1);
        // Then
        Assertions.assertNotNull(receivedProduct);
    }

    @Test
    @DisplayName("Test find all products")
    void testFindAllProducts() {
        Product mockProduct = new Product(1, "Nazwa", "Opis", 1, 1);
        Product mockProduct2 = new Product(2, "Nazwa2", "Opis2", 2, 1);
        doReturn(Arrays.asList(mockProduct, mockProduct2)).when(productRepository).findAll();
        Iterable<Product> allProducts = productService.findAll();
        Assertions.assertEquals(2, ((Collection<?>) allProducts).size());
    }

}
