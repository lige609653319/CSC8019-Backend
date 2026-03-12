package uk.ac.ncl.csc8019backend.business.loyalty.service.dto;

import uk.ac.ncl.csc8019backend.business.loyalty.entity.LoyaltyTransactionType;
import java.time.Instant;

public record LoyaltyTxDto(
        long id,
        LoyaltyTransactionType type,
        int points,
        Long orderId,
        String note,
        Instant createdAt
)
 {}