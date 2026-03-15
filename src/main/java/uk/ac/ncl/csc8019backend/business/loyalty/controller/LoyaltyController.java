package uk.ac.ncl.csc8019backend.business.loyalty.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uk.ac.ncl.csc8019backend.business.loyalty.controller.request.EarnDemoPointsRequest;
import uk.ac.ncl.csc8019backend.business.loyalty.controller.request.RedeemPointsRequest;
import uk.ac.ncl.csc8019backend.business.loyalty.service.LoyaltyService;
import uk.ac.ncl.csc8019backend.business.loyalty.service.dto.LoyaltySummaryDto;
import uk.ac.ncl.csc8019backend.business.loyalty.service.dto.LoyaltyTxDto;
import uk.ac.ncl.csc8019backend.system.common.Result;

import java.util.List;

@RestController
@RequestMapping("/api/loyalty")
@Validated
public class LoyaltyController {

    private static final String DEMO_USERNAME = "demo";

    private final LoyaltyService loyaltyService;

    public LoyaltyController(LoyaltyService loyaltyService) {
        this.loyaltyService = loyaltyService;
    }

    private String getCurrentUsername(Authentication auth) {
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            return auth.getName();
        }
        return DEMO_USERNAME;
    }

    @GetMapping("/me")
    public Result<LoyaltySummaryDto> me(Authentication auth) {
        String username = getCurrentUsername(auth);
        return Result.success(loyaltyService.getOrCreateSummary(username));
    }

    @GetMapping("/me/transactions")
    public Result<List<LoyaltyTxDto>> myTransactions(
            Authentication auth,
            @RequestParam(defaultValue = "20") @Min(value = 1, message = "limit must be at least 1") @Max(value = 50, message = "limit must not exceed 50") int limit) {
        String username = getCurrentUsername(auth);
        return Result.success(loyaltyService.getRecentTransactions(username, limit));
    }

    @PostMapping("/me/earn-demo")
    public Result<LoyaltySummaryDto> earnDemo(Authentication auth,
                                              @Valid @RequestBody EarnDemoPointsRequest request) {
        String username = getCurrentUsername(auth);
        return Result.success(loyaltyService.addDemoPoints(username, request.getPoints()));
    }

    @PostMapping("/me/redeem")
    public Result<LoyaltySummaryDto> redeem(Authentication auth,
                                            @Valid @RequestBody RedeemPointsRequest request) {
        String username = getCurrentUsername(auth);
        return Result.success(loyaltyService.redeemPoints(username, request.getPoints()));
    }
}
