package uk.ac.ncl.csc8019backend.business.store.controller;

import org.springframework.web.bind.annotation.*;
import uk.ac.ncl.csc8019backend.business.store.entity.Store;
import uk.ac.ncl.csc8019backend.business.store.service.StoreService;
import uk.ac.ncl.csc8019backend.system.common.Result;
import uk.ac.ncl.csc8019backend.business.store.dto.StoreNearbyResponse;

import java.util.List;

/**
 * Store controller
 * Provides store related APIs
 */
@RestController
@RequestMapping("/api/store")
public class StoreController {

    private final StoreService storeService;

    /**
     * Store controller constructor
     */
    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    /**
     * Get store list
     */
    @GetMapping("/list")
    public Result<List<Store>> list(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String name) {

        if (status != null && !status.trim().isEmpty()) {
            return Result.success(storeService.getStoresByStatus(status));
        }

        if (name != null && !name.trim().isEmpty()) {
            return Result.success(storeService.getStoresByName(name));
        }

        return Result.success(storeService.getAllStores());
    }

    /**
     * Get open stores
     */
    @GetMapping("/open-stores")
    public Result<List<Store>> openStores() {
        return Result.success(storeService.getOpenStores());
    }

    /**
     * Get nearby stores
     */
    @GetMapping("/nearby")
    public Result<List<StoreNearbyResponse>> nearbyStores(
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam Double radius) {
        return Result.success(storeService.getNearbyStores(latitude, longitude, radius));
    }

    /**
     * Get store by id
     */
    @GetMapping("/{id}")
    public Result<Store> get(@PathVariable Long id) {
        return Result.success(storeService.getStoreById(id));
    }

    /**
     * Create a store
     */
    @PostMapping("/create")
    public Result<Store> create(@RequestBody Store store) {
        return Result.success(storeService.createStore(store));
    }

    /**
     * Update a store
     */
    @PutMapping("/{id}")
    public Result<Store> update(@PathVariable Long id, @RequestBody Store store) {
        return Result.success(storeService.updateStore(id, store));
    }

    /**
     * Disable a store
     */
    @PatchMapping("/{id}/disable")
    public Result<Store> disable(@PathVariable Long id) {
        return Result.success(storeService.disableStore(id));
    }

    /**
     * Activate a store
     */
    @PatchMapping("/{id}/activate")
    public Result<Store> activate(@PathVariable Long id) {
        return Result.success(storeService.activateStore(id));
    }

    /**
     * Check store open status
     */
    @GetMapping("/{id}/is-open")
    public Result<Boolean> isOpen(@PathVariable Long id) {
        return Result.success(storeService.isStoreOpen(id));
    }
}