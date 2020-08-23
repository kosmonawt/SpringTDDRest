package com.tdd.integration;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.tdd.model.Product;
import com.tdd.repository.ProductRepository;
import com.tdd.service.ProductService;
import net.minidev.json.JSONUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith({SpringExtension.class})
@SpringBootTest
@AutoConfigureMockMvc
public class ProductServiceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    public void setUp() throws IOException {
        File dataJson = Paths.get("src", "test", "resources", "products.json").toFile();
        Product[] products = new ObjectMapper().readValue(dataJson, Product[].class);
        Arrays.stream(products).forEach(productRepository::save);
    }

    @AfterEach
    public void tearDown() {
        productRepository.deleteAll();
    }

    @Test
    @DisplayName("Test product found - GET /product/1")
    public void testGETProductFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/product/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    @DisplayName("Test add product - POST /product/")
    public void testPOSTProductFound() throws Exception {

        Product productToSave = new Product(21, "zazaz", "ololo", 12, 4);
        mockMvc.perform(MockMvcRequestBuilders.post("/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(productToSave)))

                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("Test add product - DELETE /product/")
    public void testDelProduct() {

    }


}
