package com.tdd.integration;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.tdd.model.Product;
import com.tdd.repository.ProductRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
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

        Product productToSave = new Product(21, "azaza", "ololo", 12, 4);
        mockMvc.perform(MockMvcRequestBuilders.post("/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(productToSave)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("Test delete product by ID - DELETE /product/{id}")
    void testDelProductByID() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/product/{id}", 1))
                .andExpect(status().isOk());
        Assertions.assertNull(productRepository.findProductById(1));

    }

    @Test
    @DisplayName("Test update product - PUT method")
    void testPutProduct() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/product/{id}", 1))
                .andExpect(status().isOk());

    }

    @Test
    public void POSTtest() {
        RestTemplate restTemplate = new RestTemplate();
        WireMockServer wireMockServer = new WireMockServer(9090);
        wireMockServer.start();
        // configure response stub
        wireMockServer.stubFor(get(urlEqualTo("/product"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody("{\n" +
                                "  \"id\": 1,\n" +
                                "  \"name\": \"First Product\",\n" +
                                "  \"description\": \"First Product Description\",\n" +
                                "  \"quantity\": 8,\n" +
                                "  \"version\": 1\n" +
                                "}")));
        //        wireMockServer.stubFor(get(urlEqualTo("/product/2"))
//                .willReturn(aResponse().withStatus(404)));
//        wireMockServer.stubFor(post("/product")
//                // Actual Header sent by the RestTemplate is: application/json;charset=UTF-8
//                .withHeader("Content-Type", containing("application/json"))
//                .withRequestBody(containing("\"id\":3"))
//                .willReturn(aResponse()
//                        .withHeader("Content-Type", "application/json")
//                        .withStatus(200)
//                        .withBodyFile("json/supply-response-after-post.json")));
        Product product = restTemplate.getForObject("http://localhost:9090/product", Product.class);
        Assertions.assertEquals(product.getId(), 1);
        wireMockServer.stop();
    }
}
