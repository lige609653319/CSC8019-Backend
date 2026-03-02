package uk.ac.ncl.csc8019backend.business.store.service;

import uk.ac.ncl.csc8019backend.business.store.entity.Store;

import java.util.List;

public interface StoreService {
    List<Store> getAllStores();
    List<Store> getStoresByStatus(String status);
    Store getStoreById(Long id);
    Store createStore(Store store);
    Store updateStore(Long id, Store store);
    Store disableStore(Long id);
    Store activateStore(Long id);
    boolean isStoreOpen(Long id);
}