package org.ecommerce.backend.controllers;

import org.ecommerce.backend.models.Transaction;
import org.ecommerce.backend.models.TransactionRequest;
import org.ecommerce.backend.models.User;
import org.ecommerce.backend.models.enums.TransactionType;
import org.ecommerce.backend.repository.UserRepository;
import org.ecommerce.backend.security.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TransactionService transactionService;

    @GetMapping("{id}")
    @PreAuthorize("#id == principal.id or hasRole('ADMIN')")
    public User getUserProfile(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return new User(user.getUsername(), user.getEmail(), user.getBalance());
    }

    @GetMapping("{id}/balance")
    @PreAuthorize("#id == principal.id or hasRole('ADMIN')")
    public BigDecimal getUserBalance(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getBalance();
    }

    @GetMapping("/{id}/transactions")
    @PreAuthorize("#id == principal.id or hasRole('ADMIN')")
    public ResponseEntity<Page<Transaction>> getUserTransactions(@PathVariable Long id, Pageable pageable) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Page<Transaction> transactions = transactionService.getTransactionsByUser(user, pageable);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/{id}/transactions/between")
    @PreAuthorize("#id == principal.id or hasRole('ADMIN')")
    public ResponseEntity<Page<Transaction>> getTransactionsBetweenForUser(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            Pageable pageable) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Page<Transaction> transactions = transactionService.getTransactionsForUserBetweenDates(user, start, end, pageable);
        return ResponseEntity.ok(transactions);
    }

    @PutMapping("/{id}/transactions/create")
    @PreAuthorize("#id == principal.id or hasRole('ADMIN')")
    public ResponseEntity<Transaction> createTransaction(
            @PathVariable Long id,
            @RequestBody TransactionRequest transactionRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Transaction transactions = transactionService.createTransaction(user, transactionRequest.getAmount(), transactionRequest.getType());
        return ResponseEntity.ok(transactions);
    }

}
