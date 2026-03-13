package uk.ac.ncl.csc8019backend.business.store.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ncl.csc8019backend.business.store.dto.StoreHoursRequest;
import uk.ac.ncl.csc8019backend.business.store.dto.StoreHoursResponse;
import uk.ac.ncl.csc8019backend.business.store.dto.StoreNearbyResponse;
import uk.ac.ncl.csc8019backend.business.store.dto.StoreResponse;
import uk.ac.ncl.csc8019backend.business.store.entity.Store;
import uk.ac.ncl.csc8019backend.business.store.entity.StoreHours;
import uk.ac.ncl.csc8019backend.business.store.repository.StoreHoursRepository;
import uk.ac.ncl.csc8019backend.business.store.repository.StoreRepository;
import uk.ac.ncl.csc8019backend.business.store.service.StoreService;
import uk.ac.ncl.csc8019backend.system.common.ResultCode;
import uk.ac.ncl.csc8019backend.system.exception.CustomException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Store service implementation
 * Handles store and store hours logic
 */
@Service
public class StoreServiceImpl implements StoreService {

    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm");

    private final StoreRepository storeRepository;
    private final StoreHoursRepository storeHoursRepository;

    /**
     * Store service constructor
     */
    public StoreServiceImpl(StoreRepository storeRepository, StoreHoursRepository storeHoursRepository) {
        this.storeRepository = storeRepository;
        this.storeHoursRepository = storeHoursRepository;
    }

    /**
     * Get all stores
     */
    @Override
    public List<StoreResponse> getAllStores() {
        int dayOfWeek = getTodayDayOfWeek();
        Map<Long, StoreHours> hoursMap = loadHoursMapByDay(dayOfWeek);

        return storeRepository.findAll().stream()
                .map(store -> toStoreResponse(store, hoursMap.get(store.getId())))
                .toList();
    }

    /**
     * Get stores by status
     */
    @Override
    public List<StoreResponse> getStoresByStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new CustomException(ResultCode.FAILED, "Status cannot be empty.");
        }

        int dayOfWeek = getTodayDayOfWeek();
        Map<Long, StoreHours> hoursMap = loadHoursMapByDay(dayOfWeek);

        return storeRepository.findByStatus(status.trim()).stream()
                .map(store -> toStoreResponse(store, hoursMap.get(store.getId())))
                .toList();
    }

    /**
     * Search stores by name
     */
    @Override
    public List<StoreResponse> getStoresByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new CustomException(ResultCode.FAILED, "Name cannot be empty.");
        }

        int dayOfWeek = getTodayDayOfWeek();
        Map<Long, StoreHours> hoursMap = loadHoursMapByDay(dayOfWeek);

        return storeRepository.findByNameContainingIgnoreCase(name.trim()).stream()
                .map(store -> toStoreResponse(store, hoursMap.get(store.getId())))
                .toList();
    }

    /**
     * Get store by id
     */
    @Override
    public StoreResponse getStoreById(Long id) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new CustomException(ResultCode.FAILED, "Store not found."));

        int dayOfWeek = getTodayDayOfWeek();
        StoreHours hours = storeHoursRepository.findByStoreIdAndDayOfWeek(store.getId(), dayOfWeek).orElse(null);

        return toStoreResponse(store, hours);
    }

    /**
     * Create store and init default weekly hours
     */
    @Override
    @Transactional
    public StoreResponse createStore(Store store) {
        if (store.getCode() == null || store.getCode().trim().isEmpty()) {
            throw new CustomException(ResultCode.FAILED, "Store code cannot be empty.");
        }

        String code = store.getCode().trim();
        if (storeRepository.findByCode(code).isPresent()) {
            throw new CustomException(ResultCode.FAILED, "Store code already exists.");
        }

        if (store.getStatus() == null || store.getStatus().trim().isEmpty()) {
            store.setStatus("ACTIVE");
        }

        store.setCode(code);
        validateCoordinates(store.getLatitude(), store.getLongitude());

        Store saved = storeRepository.save(store);

        initDefaultStoreHours(saved);

        int dayOfWeek = getTodayDayOfWeek();
        StoreHours hours = storeHoursRepository.findByStoreIdAndDayOfWeek(saved.getId(), dayOfWeek).orElse(null);

        return toStoreResponse(saved, hours);
    }

    /**
     * Update store fields
     * Store hours are not updated here
     */
    @Override
    public StoreResponse updateStore(Long id, Store store) {
        Store existing = storeRepository.findById(id)
                .orElseThrow(() -> new CustomException(ResultCode.FAILED, "Store not found."));

        if (store.getName() != null && !store.getName().trim().isEmpty()) {
            existing.setName(store.getName().trim());
        }

        existing.setLocationName(store.getLocationName());
        existing.setAddress(store.getAddress());
        existing.setLatitude(store.getLatitude());
        existing.setLongitude(store.getLongitude());
        existing.setPhone(store.getPhone());
        existing.setEmail(store.getEmail());

        if (store.getStatus() != null && !store.getStatus().trim().isEmpty()) {
            existing.setStatus(store.getStatus().trim());
        }

        validateCoordinates(existing.getLatitude(), existing.getLongitude());

        Store saved = storeRepository.save(existing);

        int dayOfWeek = getTodayDayOfWeek();
        StoreHours hours = storeHoursRepository.findByStoreIdAndDayOfWeek(saved.getId(), dayOfWeek).orElse(null);

        return toStoreResponse(saved, hours);
    }

    /**
     * Disable store
     */
    @Override
    public StoreResponse disableStore(Long id) {
        Store existing = storeRepository.findById(id)
                .orElseThrow(() -> new CustomException(ResultCode.FAILED, "Store not found."));

        existing.setStatus("INACTIVE");
        Store saved = storeRepository.save(existing);

        int dayOfWeek = getTodayDayOfWeek();
        StoreHours hours = storeHoursRepository.findByStoreIdAndDayOfWeek(saved.getId(), dayOfWeek).orElse(null);

        return toStoreResponse(saved, hours);
    }

    /**
     * Activate store
     */
    @Override
    public StoreResponse activateStore(Long id) {
        Store existing = storeRepository.findById(id)
                .orElseThrow(() -> new CustomException(ResultCode.FAILED, "Store not found."));

        existing.setStatus("ACTIVE");
        Store saved = storeRepository.save(existing);

        int dayOfWeek = getTodayDayOfWeek();
        StoreHours hours = storeHoursRepository.findByStoreIdAndDayOfWeek(saved.getId(), dayOfWeek).orElse(null);

        return toStoreResponse(saved, hours);
    }

    /**
     * Check if store is open now
     */
    @Override
    public boolean isStoreOpen(Long id) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new CustomException(ResultCode.FAILED, "Store not found."));

        if (!"ACTIVE".equalsIgnoreCase(store.getStatus())) {
            return false;
        }

        int dayOfWeek = getTodayDayOfWeek();
        LocalTime now = LocalTime.now();

        StoreHours hours = storeHoursRepository.findByStoreIdAndDayOfWeek(store.getId(), dayOfWeek).orElse(null);
        return isOpenNow(hours, now);
    }

    /**
     * Get stores that are open now
     */
    @Override
    public List<StoreResponse> getOpenStores() {
        LocalTime now = LocalTime.now();
        int dayOfWeek = getTodayDayOfWeek();
        Map<Long, StoreHours> hoursMap = loadHoursMapByDay(dayOfWeek);

        return storeRepository.findAll().stream()
                .filter(store -> "ACTIVE".equalsIgnoreCase(store.getStatus()))
                .filter(store -> isOpenNow(hoursMap.get(store.getId()), now))
                .map(store -> toStoreResponse(store, hoursMap.get(store.getId())))
                .toList();
    }

    /**
     * Get nearby stores that are open now
     */
    @Override
    public List<StoreNearbyResponse> getNearbyStores(Double latitude, Double longitude, Double radius) {
        if (latitude == null || longitude == null || radius == null) {
            throw new CustomException(ResultCode.FAILED, "Latitude longitude and radius must all be provided.");
        }
        if (radius <= 0) {
            throw new CustomException(ResultCode.FAILED, "Radius must be greater than 0.");
        }

        LocalTime now = LocalTime.now();
        int dayOfWeek = getTodayDayOfWeek();
        Map<Long, StoreHours> hoursMap = loadHoursMapByDay(dayOfWeek);

        return storeRepository.findAll().stream()
                .filter(store -> "ACTIVE".equalsIgnoreCase(store.getStatus()))
                .filter(store -> store.getLatitude() != null && store.getLongitude() != null)
                .filter(store -> isOpenNow(hoursMap.get(store.getId()), now))
                .map(store -> {
                    double distance = calculateDistance(latitude, longitude, store.getLatitude(), store.getLongitude());
                    StoreNearbyResponse item = new StoreNearbyResponse();
                    item.setStore(toStoreResponse(store, hoursMap.get(store.getId())));
                    item.setDistance(distance);
                    return item;
                })
                .filter(item -> item.getDistance() <= radius)
                .sorted((a, b) -> Double.compare(a.getDistance(), b.getDistance()))
                .toList();
    }

    /**
     * Get weekly hours for a store
     */
    @Override
    public List<StoreHoursResponse> getStoreHours(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ResultCode.FAILED, "Store not found."));

        return storeHoursRepository.findAll().stream()
                .filter(h -> h.getStore() != null && h.getStore().getId() != null && h.getStore().getId().equals(store.getId()))
                .sorted((a, b) -> Integer.compare(a.getDayOfWeek(), b.getDayOfWeek()))
                .map(StoreHoursResponse::from)
                .toList();
    }

    /**
     * Insert or update one day hours
     */
    @Override
    public StoreHoursResponse upsertStoreHours(Long storeId, StoreHoursRequest request) {
        if (request == null) {
            throw new CustomException(ResultCode.FAILED, "Request cannot be empty.");
        }
        if (request.getDayOfWeek() == null || request.getDayOfWeek() < 1 || request.getDayOfWeek() > 7) {
            throw new CustomException(ResultCode.FAILED, "Day of week must be between 1 and 7.");
        }

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ResultCode.FAILED, "Store not found."));

        StoreHours hours = storeHoursRepository.findByStoreIdAndDayOfWeek(store.getId(), request.getDayOfWeek())
                .orElseGet(StoreHours::new);

        hours.setStore(store);
        hours.setDayOfWeek(request.getDayOfWeek());

        boolean openFlag = request.getIsOpen() != null && request.getIsOpen();
        hours.setIsOpen(openFlag);

        if (openFlag) {
            validateHours(request.getOpenTime(), request.getCloseTime());
            hours.setOpenTime(parseTime(request.getOpenTime()));
            hours.setCloseTime(parseTime(request.getCloseTime()));
        } else {
            hours.setOpenTime(null);
            hours.setCloseTime(null);
        }

        StoreHours saved = storeHoursRepository.save(hours);
        return StoreHoursResponse.from(saved);
    }

    /**
     * Build today hours map
     */
    private Map<Long, StoreHours> loadHoursMapByDay(int dayOfWeek) {
        return storeHoursRepository.findByDayOfWeek(dayOfWeek).stream()
                .filter(h -> h.getStore() != null && h.getStore().getId() != null)
                .collect(Collectors.toMap(h -> h.getStore().getId(), h -> h, (a, b) -> a));
    }

    /**
     * Map store to response and fill today hours
     */
    private StoreResponse toStoreResponse(Store store, StoreHours hours) {
        StoreResponse response = StoreResponse.from(store);

        if (hours == null || hours.getIsOpen() == null || !hours.getIsOpen()) {
            response.setOpeningTime(null);
            response.setClosingTime(null);
            return response;
        }

        if (hours.getOpenTime() == null || hours.getCloseTime() == null) {
            response.setOpeningTime(null);
            response.setClosingTime(null);
            return response;
        }

        response.setOpeningTime(hours.getOpenTime().format(TIME_FMT));
        response.setClosingTime(hours.getCloseTime().format(TIME_FMT));
        return response;
    }

    /**
     * Init default weekly hours for a new store
     */
    private void initDefaultStoreHours(Store store) {
        List<StoreHours> list = new ArrayList<>();

        for (int day = 1; day <= 7; day++) {
            StoreHours hours = storeHoursRepository.findByStoreIdAndDayOfWeek(store.getId(), day)
                    .orElseGet(StoreHours::new);

            hours.setStore(store);
            hours.setDayOfWeek(day);
            hours.setIsOpen(true);

            if (day <= 5) {
                hours.setOpenTime(LocalTime.of(8, 30));
                hours.setCloseTime(LocalTime.of(21, 30));
            } else if (day == 6) {
                hours.setOpenTime(LocalTime.of(10, 0));
                hours.setCloseTime(LocalTime.of(18, 0));
            } else {
                hours.setOpenTime(LocalTime.of(10, 0));
                hours.setCloseTime(LocalTime.of(16, 0));
            }

            list.add(hours);
        }

        storeHoursRepository.saveAll(list);
    }

    /**
     * Check open status by store hours
     */
    private boolean isOpenNow(StoreHours hours, LocalTime now) {
        if (hours == null || hours.getIsOpen() == null || !hours.getIsOpen()) {
            return false;
        }
        if (hours.getOpenTime() == null || hours.getCloseTime() == null) {
            return false;
        }
        return !now.isBefore(hours.getOpenTime()) && now.isBefore(hours.getCloseTime());
    }

    /**
     * Get today day of week
     */
    private int getTodayDayOfWeek() {
        DayOfWeek dow = LocalDate.now().getDayOfWeek();
        return dow.getValue();
    }

    /**
     * Validate open and close time strings
     */
    private void validateHours(String openTime, String closeTime) {
        if (openTime == null || openTime.trim().isEmpty() || closeTime == null || closeTime.trim().isEmpty()) {
            throw new CustomException(ResultCode.FAILED, "Open time and close time must both be provided.");
        }

        LocalTime open = parseTime(openTime);
        LocalTime close = parseTime(closeTime);

        if (!open.isBefore(close)) {
            throw new CustomException(ResultCode.FAILED, "Open time must be earlier than close time.");
        }
    }

    /**
     * Parse time string
     */
    private LocalTime parseTime(String value) {
        try {
            return LocalTime.parse(value.trim());
        } catch (DateTimeParseException e) {
            throw new CustomException(ResultCode.FAILED, "Time format must be HH:mm.");
        }
    }

    /**
     * Validate latitude and longitude
     */
    private void validateCoordinates(Double latitude, Double longitude) {
        if (latitude == null && longitude == null) {
            return;
        }
        if (latitude == null || longitude == null) {
            throw new CustomException(ResultCode.FAILED, "Latitude and longitude must both be provided.");
        }
        if (latitude < -90 || latitude > 90) {
            throw new CustomException(ResultCode.FAILED, "Latitude must be between -90 and 90.");
        }
        if (longitude < -180 || longitude > 180) {
            throw new CustomException(ResultCode.FAILED, "Longitude must be between -180 and 180.");
        }
    }

    /**
     * Calculate distance in km
     */
    private double calculateDistance(Double lat1, Double lon1, Double lat2, Double lon2) {
        final double earthRadius = 6371.0;

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return earthRadius * c;
    }
}