package com.tdd.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tdd.model.Product;
import com.tdd.service.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith({SpringExtension.class})
@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

    @MockBean
    ProductService productService;

    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void setUp() throws IOException {
        File file = Paths.get("src", "test", "resources", "products.json").toFile();

        Product[] products = new ObjectMapper().readValue(file, Product[].class);

        Arrays.stream(products).forEach(productService::save);
    }

    @Test
    @DisplayName("Test product found - GET /product/1 ")
    void testGETProductFound() throws Exception {

        Product mockProduct = new Product(1, "Nazwa", "Opis", 1, 1);

        doReturn(mockProduct).when(productService).findById(mockProduct.getId());

        mockMvc.perform(MockMvcRequestBuilders.get("/product/{id}", mockProduct.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)));

    }

    @Test
    @DisplayName("Test delete product by Id- DELETE /product/{id} ")
    void testDELETEProductById() throws Exception {
        Product mockProduct = new Product(1, "Nazwa", "Opis", 1, 1);
        Product mockProduct2 = new Product(2, "Nazwa2", "Opis2", 3, 6);

        doReturn(Arrays.asList(mockProduct, mockProduct2)).when(productService).findAll();


        mockMvc.perform(MockMvcRequestBuilders.delete("/product/{id}", mockProduct.getId()))

                .andExpect(status().isOk());

        Assertions.assertNull(productService.findById(mockProduct.getId()));
        Assertions.assertNotNull((productService.findById(mockProduct2.getId())));
    }

/*    @Test
    @DisplayName("Test products found - GET /product ")
    void testGETProductsFoundAll() throws Exception {

        Product mockProduct = new Product(1, "Nazwa", "Opis", 1, 1);
        Product mockProduct2 = new Product(2, "Nazwa2", "Opis2", 3, 6);

        doReturn(Arrays.asList(mockProduct, mockProduct2)).when(productService).findAll();

        mockMvc.perform(MockMvcRequestBuilders.get("/product", mockProduct.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)));

    }*/

    @Test
    @DisplayName("Test products update - PUT /product/{id} ")
    void testUpdateProductById() throws Exception {

        Product mockProduct = new Product(1, "Nazwa", "Opis", 1, 1);
        Product updateProduct = new Product("Nazwa2", "Opis2", 32);

        // doReturn(mockProduct).when(productService).findById(1);
        doReturn(mockProduct).when(productService).update(updateProduct, 1);

        mockMvc.perform(MockMvcRequestBuilders.put("/product/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(updateProduct)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)));


    }


}
