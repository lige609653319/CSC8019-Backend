package uk.ac.ncl.csc8019backend.business.store.service.impl;

import org.springframework.stereotype.Service;
import uk.ac.ncl.csc8019backend.business.store.entity.Store;
import uk.ac.ncl.csc8019backend.business.store.repository.StoreRepository;
import uk.ac.ncl.csc8019backend.business.store.service.StoreService;
import uk.ac.ncl.csc8019backend.system.common.ResultCode;
import uk.ac.ncl.csc8019backend.system.exception.CustomException;

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

        if (store.getStatus() != null && !store.getStatus().trim().isEmpty()) {
            existingStore.setStatus(store.getStatus().trim());
        }

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
}