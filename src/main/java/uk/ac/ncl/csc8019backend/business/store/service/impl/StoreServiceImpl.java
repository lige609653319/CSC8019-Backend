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

@Service
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;

    public StoreServiceImpl(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    @Override
    public List<Store> getAllStores() {
        return storeRepository.findAll();
    }

    @Override
    public List<Store> getStoresByStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new CustomException(ResultCode.FAILED, "Status cannot be empty.");
        }

        return storeRepository.findByStatus(status.trim());
    }

    @Override
    public List<Store> getStoresByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new CustomException(ResultCode.FAILED, "Name cannot be empty.");
        }

        return storeRepository.findByNameContainingIgnoreCase(name.trim());
    }

    @Override
    public Store getStoreById(Long id) {
        return storeRepository.findById(id)
                .orElseThrow(() -> new CustomException(ResultCode.FAILED, "Store not found."));
    }

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
        validateBusinessHours(store.getOpeningTime(), store.getClosingTime());
        validateCoordinates(store.getLatitude(), store.getLongitude());

        return storeRepository.save(store);
    }

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

        if (store.getStatus() != null && !store.getStatus().trim().isEmpty()) {
            existingStore.setStatus(store.getStatus().trim());
        }

        existingStore.setOpeningTime(store.getOpeningTime());
        existingStore.setClosingTime(store.getClosingTime());

        validateBusinessHours(existingStore.getOpeningTime(), existingStore.getClosingTime());

        return storeRepository.save(existingStore);
    }

    @Override
    public Store disableStore(Long id) {
        Store existingStore = storeRepository.findById(id)
                .orElseThrow(() -> new CustomException(ResultCode.FAILED, "Store not found."));

        existingStore.setStatus("INACTIVE");
        return storeRepository.save(existingStore);
    }

    @Override
    public Store activateStore(Long id) {
        Store existingStore = storeRepository.findById(id)
                .orElseThrow(() -> new CustomException(ResultCode.FAILED, "Store not found."));

        existingStore.setStatus("ACTIVE");
        return storeRepository.save(existingStore);
    }

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
}