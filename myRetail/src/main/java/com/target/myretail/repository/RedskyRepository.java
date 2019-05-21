package com.target.myretail.repository;

import com.target.myretail.exception.ProductNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

@Repository
public class RedskyRepository {
    
    private static final String REDSKY_URL_PRE_PRODUCT_ID = "https://redsky.target.com/v2/pdp/tcin/";
    // what to exclude from JSON response
    private static final String REDSKY_URL_POST_PRODUCT_ID = "?excludes=taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics";

    /**
     * @param id the products id
     * @return the title of the product
     * @throws ProductNotFoundException
     */
    public String getProductTitleById(Integer id) throws ProductNotFoundException {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();

        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("id", id + ""); // cast id to string
        String productUrl = REDSKY_URL_PRE_PRODUCT_ID + id + REDSKY_URL_POST_PRODUCT_ID;

        try {
            // http get from redsky
            ResponseEntity<String> response = restTemplate.getForEntity(productUrl, String.class, uriVariables);
            Map<String, Map> info = mapper.readValue(response.getBody(), Map.class);

            // drill down through JSON object returned from redsky
            Map<String, Map> productMap = info.get("product");
            Map<String, Map> itemMap = productMap.get("item");
            Map<String, String> prodDescrMap = itemMap.get("product_description");
            String title = prodDescrMap.get("title");

            return title;
        } catch (Exception e) {
            throw new ProductNotFoundException("Product not found");
        }

    }
}
