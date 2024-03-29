package org.ecommerce.backend.controllers;

import org.ecommerce.backend.models.Role;
import org.ecommerce.backend.models.Transaction;
import org.ecommerce.backend.models.TransactionRequest;
import org.ecommerce.backend.models.User;
import org.ecommerce.backend.models.enums.ERole;
import org.ecommerce.backend.models.enums.TransactionStatus;
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
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/admin")
public class AdminController {

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

    @GetMapping("/allUsers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<User>> getAllUsers(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        return ResponseEntity.ok(users);
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

    @PutMapping("/transactions/{transactionId}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> approveTransaction(@PathVariable Long transactionId) {
        Transaction transaction = transactionService.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        if (transaction.getStatus() != TransactionStatus.PENDING) {
            return ResponseEntity.badRequest().body("Transaction is not in pending status");
        }

        transaction.setStatus(TransactionStatus.COMPLETED);
        Transaction updatedTransaction = transactionService.saveTransaction(transaction);
        return ResponseEntity.ok(updatedTransaction);
    }

    @PutMapping("/transactions/{transactionId}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> rejectTransaction(@PathVariable Long transactionId) {
        Transaction transaction = transactionService.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        if (transaction.getStatus() != TransactionStatus.PENDING) {
            return ResponseEntity.badRequest().body("Transaction is not in pending status");
        }

        transaction.setStatus(TransactionStatus.REJECTED);
        Transaction updatedTransaction = transactionService.saveTransaction(transaction);
        return ResponseEntity.ok(updatedTransaction);
    }

}
