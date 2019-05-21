package com.target.myretail.repository;

import com.target.myretail.exception.ProductNotFoundException;
import com.target.myretail.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends MongoRepository<Product, Integer> { }
