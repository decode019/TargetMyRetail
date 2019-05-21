package com.target.myretail;

import com.target.myretail.exception.ProductNotFoundException;
import com.target.myretail.model.CurrentPrice;
import com.target.myretail.model.Product;
import com.target.myretail.repository.ProductRepository;
import com.target.myretail.repository.RedskyRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MyRetailTests {

    @Autowired
    private ProductRepository repository;
    @Autowired
    private RedskyRepository redskyRepository;

    @After
    public void cleanup() {
        for (Product product : repository.findAll()) {
            repository.delete(product);
        }
    }

    private Product getTestProduct() {
        Product product = new Product();
        product.setId(13860428);
        product.setName("The Big Lebowski (Blu-ray)");
        product.setCurrentPrice(new CurrentPrice(14.99, "USA"));
        return product;
    }

    @Test
    public void testMongoDBSave() {
        Product product = getTestProduct();
        Product saveResult = repository.save(product);
        assertEquals(product, saveResult);
        Product findResult = repository.findById(13860428).orElse(null);
        assertNotNull(findResult);
        assertEquals(null, findResult.getName());
        findResult.setName(product.getName());
        assertEquals(product.toString(), findResult.toString());
    }

    @Test
    public void testIdNotFoundMongoDB() {
        repository.save(getTestProduct());
        
        Product result = repository.findById(0).orElse(null);
        assertEquals(null, result);
        result = repository.findById(-1).orElse(null);
        assertEquals(null, result);
        result = repository.findById(1386042).orElse(null);
        assertEquals(null, result);
    }

    @Test
    public void testRedskyGetTitle() throws ProductNotFoundException {
        String title = redskyRepository.getProductTitleById(13860428);
        assertEquals("The Big Lebowski (Blu-ray)", title);
    }

    @Test
    public void testRedskyProductNotFound() {
        try {
            String title = redskyRepository.getProductTitleById(0);
            fail("should have found error for id " + 0);
        } catch (ProductNotFoundException e) {
        }
    }

}
