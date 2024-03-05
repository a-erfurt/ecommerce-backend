package org.ecommerce.backend.repository;

import org.ecommerce.backend.models.Transaction;
import org.ecommerce.backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Page<Transaction> findByUserOrderByDateTimeDesc(User user, Pageable pageable);
    List<Transaction> findByDateTimeBetween(LocalDateTime start, LocalDateTime end);
    List<Transaction> findByUserId(Long userId);
    Page<Transaction> findByUserAndDateTimeBetween(User user, LocalDateTime start, LocalDateTime end, Pageable pageable);

}
