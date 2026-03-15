package uk.ac.ncl.csc8019backend.business.store.dto;

import lombok.Data;

@Data
public class StoreHoursRequest {

    private Integer dayOfWeek;

    private Boolean isOpen;

    private String openTime;

    private String closeTime;
}