package uk.ac.ncl.csc8019backend.business.store.dto;

import lombok.Data;
import uk.ac.ncl.csc8019backend.business.store.entity.StoreHours;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Data
public class StoreHoursResponse {

    private Integer dayOfWeek;

    private Boolean isOpen;

    private String openTime;

    private String closeTime;

    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

    public static StoreHoursResponse from(StoreHours hours) {
        StoreHoursResponse response = new StoreHoursResponse();
        response.setDayOfWeek(hours.getDayOfWeek());
        response.setIsOpen(hours.getIsOpen());
        response.setOpenTime(formatTime(hours.getOpenTime()));
        response.setCloseTime(formatTime(hours.getCloseTime()));
        return response;
    }

    public static StoreHoursResponse closed(Integer dayOfWeek) {
        StoreHoursResponse response = new StoreHoursResponse();
        response.setDayOfWeek(dayOfWeek);
        response.setIsOpen(false);
        response.setOpenTime(null);
        response.setCloseTime(null);
        return response;
    }

    private static String formatTime(LocalTime time) {
        if (time == null) {
            return null;
        }
        return time.format(TIME_FORMAT);
    }
}