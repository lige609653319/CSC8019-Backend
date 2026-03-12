package uk.ac.ncl.csc8019backend.business.loyalty.service;

import uk.ac.ncl.csc8019backend.business.loyalty.service.dto.LoyaltySummaryDto;
import uk.ac.ncl.csc8019backend.business.loyalty.service.dto.LoyaltyTxDto;

import java.util.List;

public interface LoyaltyService {

    LoyaltySummaryDto getOrCreateSummary(String username);

    List<LoyaltyTxDto> getRecentTransactions(String username, int limit);

    int calculateEarnPoints(double orderTotal);

    void awardPointsForCompletedOrder(String username, Long orderId, double orderTotal);

    LoyaltySummaryDto addDemoPoints(String username, Integer points);

    LoyaltySummaryDto redeemPoints(String username, Integer pointsToRedeem);
}
