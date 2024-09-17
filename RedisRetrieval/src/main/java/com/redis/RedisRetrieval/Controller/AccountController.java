package com.redis.RedisRetrieval.Controller;

import com.redis.RedisRetrieval.Entity.Account;
import com.redis.RedisRetrieval.Service.AccountService;
import com.redis.RedisRetrieval.Exceptions.DataNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;


    @GetMapping("/{key}")
    public ResponseEntity<Account> getAccount(@PathVariable String key) {
        Account account = accountService.getAccountData(key);
        return new ResponseEntity<>(account, HttpStatus.OK);
    }


    @PostMapping("/{key}")
    public ResponseEntity<String> storeAccount(@PathVariable String key, @RequestBody Account account) {
        boolean success = accountService.storeAccountInfo(key, account);
        if (success) {
            return new ResponseEntity<>("Account information stored successfully.", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Failed to store account information.", HttpStatus.BAD_REQUEST);
        }
    }


    @PutMapping("/{key}")
    public ResponseEntity<String> updateAccount(@PathVariable String key, @RequestBody Account account) {
        boolean success = accountService.updateAccountInfo(key, account);
        if (success) {
            return new ResponseEntity<>("Account information updated successfully.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Failed to update account information.", HttpStatus.BAD_REQUEST);
        }
    }


    @DeleteMapping("/{key}")
    public ResponseEntity<String> deleteAccount(@PathVariable String key) {
        boolean success = accountService.deleteAccountInfo(key);
        if (success) {
            return new ResponseEntity<>("Account deleted successfully.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Failed to delete account.", HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/all")
    public ResponseEntity<List<Account>> getAllAccountsByPattern() {
        List<Account> accounts = accountService.getAllAccountsByPattern();
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }


    @GetMapping("/sort")
    public ResponseEntity<List<Account>> sortProfiles(@RequestParam String sortBy) {
        try {
            List<Account> sortedAccounts = accountService.sortProfiles(sortBy);
            return new ResponseEntity<>(sortedAccounts, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
