package uk.ac.ncl.csc8019backend.business.loyalty.controller.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class RedeemPointsRequest {

    @NotNull(message = "Points is required")
    @Min(value = 1, message = "Points must be greater than 0")
    @Max(value = 1_000_000, message = "Points must not exceed 1000000")
    private Integer points;

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }
}