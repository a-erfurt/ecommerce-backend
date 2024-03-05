package org.ecommerce.backend.models;

import org.ecommerce.backend.models.enums.TransactionType;

import java.math.BigDecimal;

public class TransactionRequest {
    private BigDecimal amount;
    private TransactionType type;

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
}