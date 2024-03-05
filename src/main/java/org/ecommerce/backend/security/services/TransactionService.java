package org.ecommerce.backend.security.services;

import org.springframework.transaction.annotation.Transactional;
import org.ecommerce.backend.models.Transaction;
import org.ecommerce.backend.models.User;
import org.ecommerce.backend.models.enums.TransactionStatus;
import org.ecommerce.backend.models.enums.TransactionType;
import org.ecommerce.backend.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private UserDetailsServiceImpl userService;

    public Transaction saveTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public Page<Transaction> getTransactionsByUser(User user, Pageable pageable) {
        return transactionRepository.findByUserOrderByDateTimeDesc(user, pageable);
    }

    @Transactional(readOnly = true)
    public List<Transaction> getTransactionsBetweenDates(LocalDateTime start, LocalDateTime end) {
        return transactionRepository.findByDateTimeBetween(start, end);
    }

    public BigDecimal calculateTotalAmount(List<Transaction> transactions) {
        return transactions.stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Transactional
    public Transaction createTransaction(User user, BigDecimal amount, TransactionType type) {
        if (type == TransactionType.WITHDRAWAL && user.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setAmount(amount);
        transaction.setType(type);
        transaction.setStatus(TransactionStatus.PENDING);
        transaction.setDateTime(LocalDateTime.now());
        Transaction savedTransaction = transactionRepository.save(transaction);

        if (type == TransactionType.DEPOSIT) {
            userService.updateBalance(user, amount);
        } else if (type == TransactionType.WITHDRAWAL) {
            userService.updateBalance(user, amount.negate());
        }

        return savedTransaction;
    }

    @Transactional(readOnly = true)
    public List<Transaction> getUserTransactions(Long userId) {
        return transactionRepository.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public Page<Transaction> getTransactionsForUserBetweenDates(User user, LocalDateTime start, LocalDateTime end, Pageable pageable) {
        return transactionRepository.findByUserAndDateTimeBetween(user, start, end, pageable);
    }

}