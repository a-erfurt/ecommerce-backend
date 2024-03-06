package org.ecommerce.backend.controllers;

import org.ecommerce.backend.models.Transaction;
import org.ecommerce.backend.repository.UserRepository;
import org.ecommerce.backend.security.services.TransactionService;
import org.ecommerce.backend.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsServiceImpl userService;

    @PostMapping("/")
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
        Transaction savedTransaction = transactionService.saveTransaction(transaction);
        return ResponseEntity.ok(savedTransaction);
    }

}
