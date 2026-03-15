package uk.ac.ncl.csc8019backend.business.store.controller;

import org.springframework.web.bind.annotation.*;
import uk.ac.ncl.csc8019backend.business.store.dto.StoreHoursRequest;
import uk.ac.ncl.csc8019backend.business.store.dto.StoreHoursResponse;
import uk.ac.ncl.csc8019backend.business.store.dto.StoreNearbyResponse;
import uk.ac.ncl.csc8019backend.business.store.dto.StoreResponse;
import uk.ac.ncl.csc8019backend.business.store.entity.Store;
import uk.ac.ncl.csc8019backend.business.store.service.StoreService;
import uk.ac.ncl.csc8019backend.system.common.Result;

import java.util.List;

@RestController
@RequestMapping("/api/store")
public class StoreController {

    private final StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    /**
     * List stores
     */
    @GetMapping("/list")
    public Result<List<StoreResponse>> list(
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
     * List stores that are open now
     */
    @GetMapping("/open-stores")
    public Result<List<StoreResponse>> openStores() {
        return Result.success(storeService.getOpenStores());
    }

    /**
     * List nearby stores that are open now
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
    public Result<StoreResponse> get(@PathVariable Long id) {
        return Result.success(storeService.getStoreById(id));
    }

    /**
     * Create store
     */
    @PostMapping("/create")
    public Result<StoreResponse> create(@RequestBody Store store) {
        return Result.success(storeService.createStore(store));
    }

    /**
     * Update store
     */
    @PutMapping("/{id}")
    public Result<StoreResponse> update(@PathVariable Long id, @RequestBody Store store) {
        return Result.success(storeService.updateStore(id, store));
    }

    /**
     * Disable store
     */
    @PatchMapping("/{id}/disable")
    public Result<StoreResponse> disable(@PathVariable Long id) {
        return Result.success(storeService.disableStore(id));
    }

    /**
     * Activate store
     */
    @PatchMapping("/{id}/activate")
    public Result<StoreResponse> activate(@PathVariable Long id) {
        return Result.success(storeService.activateStore(id));
    }

    /**
     * Check store open status
     */
    @GetMapping("/{id}/is-open")
    public Result<Boolean> isOpen(@PathVariable Long id) {
        return Result.success(storeService.isStoreOpen(id));
    }

    /**
     * Get store weekly hours
     */
    @GetMapping("/{id}/hours")
    public Result<List<StoreHoursResponse>> hours(@PathVariable Long id) {
        return Result.success(storeService.getStoreHours(id));
    }

    /**
     * Set store hours for one day
     */
    @PutMapping("/{id}/hours")
    public Result<StoreHoursResponse> upsertHours(@PathVariable Long id, @RequestBody StoreHoursRequest request) {
        return Result.success(storeService.upsertStoreHours(id, request));
    }
}