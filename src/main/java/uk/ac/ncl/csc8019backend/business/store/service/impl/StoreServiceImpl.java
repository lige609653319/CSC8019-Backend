package uk.ac.ncl.csc8019backend.business.store.service.impl;

import org.springframework.stereotype.Service;
import uk.ac.ncl.csc8019backend.business.store.entity.Store;
import uk.ac.ncl.csc8019backend.business.store.repository.StoreRepository;
import uk.ac.ncl.csc8019backend.business.store.service.StoreService;
import uk.ac.ncl.csc8019backend.system.common.ResultCode;
import uk.ac.ncl.csc8019backend.system.exception.CustomException;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Store service implementation
 * Handles store business rules
 */
@Service
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;

    /**
     * Store service constructor
     */
    public StoreServiceImpl(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    /**
     * Get all stores
     */
    @Override
    public List<Store> getAllStores() {
        return storeRepository.findAll();
    }

    /**
     * Get stores by status
     */
    @Override
    public List<Store> getStoresByStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new CustomException(ResultCode.FAILED, "Status cannot be empty.");
        }

        return storeRepository.findByStatus(status.trim());
    }

    /**
     * Get stores by name
     */
    @Override
    public List<Store> getStoresByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new CustomException(ResultCode.FAILED, "Name cannot be empty.");
        }

        return storeRepository.findByNameContainingIgnoreCase(name.trim());
    }

    /**
     * Get store by id
     */
    @Override
    public Store getStoreById(Long id) {
        return storeRepository.findById(id)
                .orElseThrow(() -> new CustomException(ResultCode.FAILED, "Store not found."));
    }

    /**
     * Create a store
     */
    @Override
    public Store createStore(Store store) {
        if (store.getCode() == null || store.getCode().trim().isEmpty()) {
            throw new CustomException(ResultCode.FAILED, "Store code cannot be empty.");
        }

        if (storeRepository.findByCode(store.getCode().trim()).isPresent()) {
            throw new CustomException(ResultCode.FAILED, "Store code already exists.");
        }

        if (store.getStatus() == null || store.getStatus().trim().isEmpty()) {
            store.setStatus("ACTIVE");
        }

        store.setCode(store.getCode().trim());

        // Validate business hours before saving
        validateBusinessHours(store.getOpeningTime(), store.getClosingTime());

        // Validate coordinates before saving
        validateCoordinates(store.getLatitude(), store.getLongitude());

        return storeRepository.save(store);
    }

    /**
     * Update a store
     */
    @Override
    public Store updateStore(Long id, Store store) {
        Store existingStore = storeRepository.findById(id)
                .orElseThrow(() -> new CustomException(ResultCode.FAILED, "Store not found."));

        if (store.getName() != null && !store.getName().trim().isEmpty()) {
            existingStore.setName(store.getName().trim());
        }

        existingStore.setLocationName(store.getLocationName());
        existingStore.setAddress(store.getAddress());
        existingStore.setLatitude(store.getLatitude());
        existingStore.setLongitude(store.getLongitude());
        existingStore.setPhone(store.getPhone());
        existingStore.setEmail(store.getEmail());

        if (store.getStatus() != null && !store.getStatus().trim().isEmpty()) {
            existingStore.setStatus(store.getStatus().trim());
        }

        existingStore.setOpeningTime(store.getOpeningTime());
        existingStore.setClosingTime(store.getClosingTime());

        // Validate business hours before saving
        validateBusinessHours(existingStore.getOpeningTime(), existingStore.getClosingTime());

        // Validate coordinates before saving
        validateCoordinates(existingStore.getLatitude(), existingStore.getLongitude());

        return storeRepository.save(existingStore);
    }

    /**
     * Disable a store
     */
    @Override
    public Store disableStore(Long id) {
        Store existingStore = storeRepository.findById(id)
                .orElseThrow(() -> new CustomException(ResultCode.FAILED, "Store not found."));

        existingStore.setStatus("INACTIVE");
        return storeRepository.save(existingStore);
    }

    /**
     * Activate a store
     */
    @Override
    public Store activateStore(Long id) {
        Store existingStore = storeRepository.findById(id)
                .orElseThrow(() -> new CustomException(ResultCode.FAILED, "Store not found."));

        existingStore.setStatus("ACTIVE");
        return storeRepository.save(existingStore);
    }

    /**
     * Check store open status
     */
    @Override
    public boolean isStoreOpen(Long id) {
        Store existingStore = storeRepository.findById(id)
                .orElseThrow(() -> new CustomException(ResultCode.FAILED, "Store not found."));

        if (!"ACTIVE".equalsIgnoreCase(existingStore.getStatus())) {
            return false;
        }

        String openingTime = existingStore.getOpeningTime();
        String closingTime = existingStore.getClosingTime();

        if (openingTime == null || openingTime.trim().isEmpty()
                || closingTime == null || closingTime.trim().isEmpty()) {
            return false;
        }

        try {
            LocalTime open = LocalTime.parse(openingTime.trim());
            LocalTime close = LocalTime.parse(closingTime.trim());
            LocalTime now = LocalTime.now();

            return !now.isBefore(open) && now.isBefore(close);
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Get open stores
     */
    @Override
    public List<Store> getOpenStores() {
        LocalTime now = LocalTime.now();

        return storeRepository.findAll().stream()
                .filter(store -> "ACTIVE".equalsIgnoreCase(store.getStatus()))
                .filter(store -> store.getOpeningTime() != null && !store.getOpeningTime().trim().isEmpty())
                .filter(store -> store.getClosingTime() != null && !store.getClosingTime().trim().isEmpty())
                .filter(store -> {
                    try {
                        LocalTime open = LocalTime.parse(store.getOpeningTime().trim());
                        LocalTime close = LocalTime.parse(store.getClosingTime().trim());
                        return !now.isBefore(open) && now.isBefore(close);
                    } catch (DateTimeParseException e) {
                        return false;
                    }
                })
                .toList();
    }

    /**
     * Get nearby stores
     */
    @Override
    public List<Store> getNearbyStores(Double latitude, Double longitude, Double radius) {
        if (latitude == null || longitude == null || radius == null) {
            throw new CustomException(ResultCode.FAILED, "Latitude, longitude and radius must all be provided.");
        }

        if (radius <= 0) {
            throw new CustomException(ResultCode.FAILED, "Radius must be greater than 0.");
        }

        LocalTime now = LocalTime.now();

        // Keep active stores with valid hours and coordinates
        return storeRepository.findAll().stream()
                .filter(store -> "ACTIVE".equalsIgnoreCase(store.getStatus()))
                .filter(store -> store.getOpeningTime() != null && !store.getOpeningTime().trim().isEmpty())
                .filter(store -> store.getClosingTime() != null && !store.getClosingTime().trim().isEmpty())
                .filter(store -> store.getLatitude() != null && store.getLongitude() != null)
                .filter(store -> {
                    try {
                        LocalTime open = LocalTime.parse(store.getOpeningTime().trim());
                        LocalTime close = LocalTime.parse(store.getClosingTime().trim());
                        return !now.isBefore(open) && now.isBefore(close);
                    } catch (DateTimeParseException e) {
                        return false;
                    }
                })
                .filter(store -> calculateDistance(latitude, longitude, store.getLatitude(), store.getLongitude()) <= radius)
                .sorted((a, b) -> Double.compare(
                        calculateDistance(latitude, longitude, a.getLatitude(), a.getLongitude()),
                        calculateDistance(latitude, longitude, b.getLatitude(), b.getLongitude())
                ))
                .toList();
    }

    /**
     * Validate business hours
     */
    private void validateBusinessHours(String openingTime, String closingTime) {
        boolean openingBlank = openingTime == null || openingTime.trim().isEmpty();
        boolean closingBlank = closingTime == null || closingTime.trim().isEmpty();

        if (openingBlank && closingBlank) {
            return;
        }

        if (openingBlank || closingBlank) {
            throw new CustomException(ResultCode.FAILED, "Opening time and closing time must both be provided.");
        }

        try {
            LocalTime open = LocalTime.parse(openingTime.trim());
            LocalTime close = LocalTime.parse(closingTime.trim());

            if (!open.isBefore(close)) {
                throw new CustomException(ResultCode.FAILED, "Opening time must be earlier than closing time.");
            }
        } catch (DateTimeParseException e) {
            throw new CustomException(ResultCode.FAILED, "Time format must be HH:mm.");
        }
    }

    /**
     * Validate coordinates
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
     * Calculate distance in kilometers
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