package org.ecommerce.backend.controllers;

import org.ecommerce.backend.models.Transaction;
import org.ecommerce.backend.models.User;
import org.ecommerce.backend.models.enums.TransactionType;
import org.ecommerce.backend.repository.UserRepository;
import org.ecommerce.backend.security.services.TransactionService;
import org.ecommerce.backend.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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
