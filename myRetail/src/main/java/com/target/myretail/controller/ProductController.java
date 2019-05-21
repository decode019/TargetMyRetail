package com.target.myretail.controller;

import com.target.myretail.exception.ProductNotFoundException;
import com.target.myretail.exception.UpdateIntegrityException;
import com.target.myretail.model.CurrentPrice;
import com.target.myretail.model.Product;
import com.target.myretail.repository.ProductRepository;
import com.target.myretail.repository.RedskyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product/v1")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private RedskyRepository redskyRepository;

    @GetMapping(value = "/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Integer id) throws ProductNotFoundException {

        String title = redskyRepository.getProductTitleById(id);
        if(title==null){
            throw new ProductNotFoundException("Could not find product title");
        }

        Product dbData = productRepository.findById(id).orElse(null);
        if (dbData == null) {
            throw new ProductNotFoundException("Could not find product pricing");
        }

        return new ResponseEntity<>(new Product(id, title, dbData.getCurrentPrice()), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/price")
    public ResponseEntity<CurrentPrice> getProductPrice(@PathVariable Integer id) throws ProductNotFoundException {

        Product dbData = productRepository.findById(id).orElse(null);
        if (dbData == null) {
            throw new ProductNotFoundException("Could not find product pricing");
        }
        return new ResponseEntity<>(dbData.getCurrentPrice(), HttpStatus.OK);

    }

    @PostMapping
    public ResponseEntity<String> saveProductPrice(@RequestBody Product product) {

        if (product == null) {
            return new ResponseEntity<>("Product body required", HttpStatus.BAD_REQUEST);
        }

        productRepository.save(product);
        return new ResponseEntity<>("Pricing saved successfully", HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<String> updateProductPrice(@PathVariable Integer id,
                                        @RequestBody Product product) throws UpdateIntegrityException {
        if (id != product.getId()) {
            throw new UpdateIntegrityException("IDs are mismatched");
        }

        if (productRepository.existsById(id) == false) {
            throw new UpdateIntegrityException("Invalid ID");
        }

        productRepository.save(product);
        return new ResponseEntity<>("Pricing updated successfully for ProductId: "+id, HttpStatus.OK);
    }
}
