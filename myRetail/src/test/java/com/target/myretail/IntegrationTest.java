package com.target.myretail;

import com.target.myretail.model.CurrentPrice;
import com.target.myretail.model.Product;
import com.target.myretail.repository.ProductRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class IntegrationTest {

    @Autowired
    private ProductRepository repository;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    private Product getTestProduct() {
        Product p = new Product();
        p.setId(13860428);
        p.setName("The Big Lebowski (Blu-ray)");
        p.setCurrentPrice(new CurrentPrice(14.99, "USA"));
        return p;
    }

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).dispatchOptions(true).build();
        Product product = getTestProduct();
        repository.save(product);
    }

    @After
    public void teardown() {
        // clear database
        for (Product p : repository.findAll()) {
            repository.delete(p);
        }
    }

    @Test
    public void testGetProduct() throws Exception {
        mockMvc
                .perform(get("/product/v1/13860428"))
                .andExpect(status().isOk())
                .andExpect(content().json("{id:13860428,name:'The Big Lebowski (Blu-ray)',currentPrice:{value:14.99,currencyCode:'USA'}}"));
    }

    @Test
    public void testGetProductNotFound() throws Exception {
        mockMvc
                .perform(get("/product/v1/-1"))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{message:'Product not found'}"));
    }

    @Test
    public void testUpdateProduct() throws Exception {
        Product updatedProduct = getTestProduct();
        updatedProduct.getCurrentPrice().setValue(19.99);
        String updatedProductJSON = new ObjectMapper().writeValueAsString(updatedProduct);
        mockMvc
                .perform(put("/product/v1/13860428").contentType(MediaType.APPLICATION_JSON).content(updatedProductJSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testSaveProduct() throws Exception {
        Product product = getTestProduct();
        product.getCurrentPrice().setValue(19.99);
        String productJSON = new ObjectMapper().writeValueAsString(product);
        mockMvc
                .perform(post("/product/v1").contentType(MediaType.APPLICATION_JSON).content(productJSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testPutIdThrowsIdMismatch() throws Exception{
        Product updatedProduct = getTestProduct();
        updatedProduct.getCurrentPrice().setValue(19.99);
        updatedProduct.setId(-1);
        String updatedProductJSON = new ObjectMapper().writeValueAsString(updatedProduct);
        mockMvc
                .perform(put("/product/v1/13860428").contentType(MediaType.APPLICATION_JSON).content(updatedProductJSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().json("{message:'IDs are mismatched'}"));
    }

    @Test
    public void testPutIdThrowsUnknownId() throws Exception{
        Product updatedProduct = getTestProduct();
        updatedProduct.getCurrentPrice().setValue(19.99);
        updatedProduct.setId(-1);
        String updatedProductJSON = new ObjectMapper().writeValueAsString(updatedProduct);
        mockMvc
                .perform(put("/product/v1/-1").contentType(MediaType.APPLICATION_JSON).content(updatedProductJSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().json("{message:'Invalid ID'}"));
    }
}
