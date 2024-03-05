package org.ecommerce.backend.models;

import jakarta.persistence.*;
import org.ecommerce.backend.models.enums.TransactionStatus;
import org.ecommerce.backend.models.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private TransactionType type;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private TransactionStatus status;

    @Column(nullable = false)
    private LocalDateTime dateTime;

    public Transaction() {
    }

    public Transaction(User user, BigDecimal amount, TransactionType type, TransactionStatus status, LocalDateTime dateTime) {
        this.user = user;
        this.amount = amount;
        this.type = type;
        this.status = status;
        this.dateTime = dateTime;
    }

    // Getter und Setter
    // ...

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
