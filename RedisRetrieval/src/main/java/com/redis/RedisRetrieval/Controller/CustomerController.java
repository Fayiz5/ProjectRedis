package com.redis.RedisRetrieval.Controller;


import com.redis.RedisRetrieval.Entity.Customer;
import com.redis.RedisRetrieval.Service.CustomerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.beans.Customizer;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {
    private static final Logger logger = LogManager.getLogger(CustomerController.class);
    @Autowired
    private CustomerService customerService;

    @PostMapping("/{key}")
    public ResponseEntity<String> AddCustomertoRedis(@PathVariable String key, @RequestBody Customer customer)
    {
        boolean success= customerService.storeCustomerInfo(key, customer);
        if (success) {
            return new ResponseEntity<>("Customer information stored successfully.", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Failed to store Customer information.", HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("{key}")
    public ResponseEntity<Customer> GetCustomerFromRedis(@PathVariable String key) {
        try {
            Customer customer = customerService.getCustomerData(key);

            if (customer != null) {
                return ResponseEntity.ok(customer);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Empty body for 404
            }

        } catch (Exception e) {
            logger.error("Error retrieving customer data for key: {}", key, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 error response
        }
    }

    @PutMapping("/{key}")
    public ResponseEntity<String> UpdateCustomer(@PathVariable String key,@RequestBody Customer customer)
    {
        boolean success= customerService.updatecustomerInfo(key, customer);
        if (success) 
            return new ResponseEntity<>("Customer information Updated successfully.", HttpStatus.CREATED);
       
        return new ResponseEntity<>("Failed to Update Customer information.", HttpStatus.BAD_REQUEST);
      
    }
    
    @DeleteMapping("/{key}")
    public ResponseEntity<String> DeleteCustomer(@PathVariable String key)
    {
        boolean success = customerService.deleteCustomerInfo(key);
        if (success)
            return new ResponseEntity<>("Customer information Deleted successfully.", HttpStatus.CREATED);

        return new ResponseEntity<>("Failed to Delete Customer information.", HttpStatus.BAD_REQUEST);


    }
}
