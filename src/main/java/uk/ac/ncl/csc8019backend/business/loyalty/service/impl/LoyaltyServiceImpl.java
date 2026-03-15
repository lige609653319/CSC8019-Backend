package uk.ac.ncl.csc8019backend.business.loyalty.service.impl;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ncl.csc8019backend.business.loyalty.entity.LoyaltyAccount;
import uk.ac.ncl.csc8019backend.business.loyalty.entity.LoyaltyTransaction;
import uk.ac.ncl.csc8019backend.business.loyalty.entity.LoyaltyTransactionType;
import uk.ac.ncl.csc8019backend.business.loyalty.repository.LoyaltyAccountRepository;
import uk.ac.ncl.csc8019backend.business.loyalty.repository.LoyaltyTransactionRepository;
import uk.ac.ncl.csc8019backend.business.loyalty.service.LoyaltyService;
import uk.ac.ncl.csc8019backend.business.loyalty.service.dto.LoyaltySummaryDto;
import uk.ac.ncl.csc8019backend.business.loyalty.service.dto.LoyaltyTxDto;
import uk.ac.ncl.csc8019backend.system.exception.CustomException;

import java.util.List;

@Service
public class LoyaltyServiceImpl implements LoyaltyService {

    private final LoyaltyAccountRepository accountRepo;
    private final LoyaltyTransactionRepository txRepo;

    public LoyaltyServiceImpl(LoyaltyAccountRepository accountRepo, LoyaltyTransactionRepository txRepo) {
        this.accountRepo = accountRepo;
        this.txRepo = txRepo;
    }

    @Override
    @Transactional
    public LoyaltySummaryDto getOrCreateSummary(String username) {
        LoyaltyAccount acc = accountRepo.findByUsername(username)
                .orElseGet(() -> accountRepo.save(new LoyaltyAccount(username)));

        return new LoyaltySummaryDto(acc.getUsername(), acc.getPointsBalance());
    }

    @Override
    public List<LoyaltyTxDto> getRecentTransactions(String username, int limit) {
        var page = txRepo.findByUsernameOrderByCreatedAtDesc(
                username,
                PageRequest.of(0, Math.min(limit, 50))
        );

        return page.getContent().stream()
                .map(tx -> new LoyaltyTxDto(
                        tx.getId(),
                        tx.getType(),
                        tx.getPoints(),
                        tx.getOrderId(),
                        tx.getNote(),
                        tx.getCreatedAt()
                ))
                .toList();
    }

    @Override
    public int calculateEarnPoints(double orderTotal) {
        if (orderTotal <= 0) {
            return 0;
        }
        return (int) Math.floor(orderTotal);
    }

    @Override
    @Transactional
    public void awardPointsForCompletedOrder(String username, Long orderId, double orderTotal) {
        int points = calculateEarnPoints(orderTotal);
        if (points <= 0) {
            return;
        }

        LoyaltyAccount acc = accountRepo.findByUsername(username)
                .orElseGet(() -> accountRepo.save(new LoyaltyAccount(username)));

        acc.setPointsBalance(acc.getPointsBalance() + points);
        accountRepo.save(acc);

        txRepo.save(new LoyaltyTransaction(
                username,
                orderId,
                LoyaltyTransactionType.EARN,
                points,
                "Earned from completed order"
        ));
    }

    @Override
    @Transactional
    public LoyaltySummaryDto addDemoPoints(String username, Integer points) {
        if (points == null || points <= 0) {
            throw new CustomException("Points must be greater than zero");
        }

        LoyaltyAccount acc = accountRepo.findByUsername(username)
                .orElseGet(() -> accountRepo.save(new LoyaltyAccount(username)));

        acc.setPointsBalance(acc.getPointsBalance() + points);
        accountRepo.save(acc);

        txRepo.save(new LoyaltyTransaction(
                username,
                null,
                LoyaltyTransactionType.ADJUST,
                points,
                "Demo points added"
        ));

        return new LoyaltySummaryDto(acc.getUsername(), acc.getPointsBalance());
    }

    @Override
    @Transactional
    public LoyaltySummaryDto redeemPoints(String username, Integer pointsToRedeem) {
        if (pointsToRedeem == null || pointsToRedeem <= 0) {
            throw new CustomException("Points must be greater than zero");
        }

        LoyaltyAccount acc = accountRepo.findByUsername(username)
                .orElseGet(() -> accountRepo.save(new LoyaltyAccount(username)));

        if (acc.getPointsBalance() < pointsToRedeem) {
            throw new CustomException("Not enough points");
        }

        acc.setPointsBalance(acc.getPointsBalance() - pointsToRedeem);
        accountRepo.save(acc);

        txRepo.save(new LoyaltyTransaction(
                username,
                null,
                LoyaltyTransactionType.REDEEM,
                -pointsToRedeem,
                "Redeemed points"
        ));

        return new LoyaltySummaryDto(acc.getUsername(), acc.getPointsBalance());
    }
}
