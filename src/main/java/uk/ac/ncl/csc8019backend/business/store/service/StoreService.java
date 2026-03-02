package uk.ac.ncl.csc8019backend.business.store.service;

import uk.ac.ncl.csc8019backend.business.store.entity.Store;

import java.util.List;

public interface StoreService {
    List<Store> getAllStores();
    Store getStoreById(Long id);
    Store createStore(Store store);
    Store updateStore(Long id, Store store);
}