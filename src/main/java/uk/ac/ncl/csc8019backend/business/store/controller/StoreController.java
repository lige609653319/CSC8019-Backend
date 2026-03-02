package uk.ac.ncl.csc8019backend.business.store.controller;

import org.springframework.web.bind.annotation.*;
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

    @GetMapping("/list")
    public Result<List<Store>> list(@RequestParam(required = false) String status) {
        if (status == null || status.trim().isEmpty()) {
            return Result.success(storeService.getAllStores());
        }
        return Result.success(storeService.getStoresByStatus(status));
    }

    @GetMapping("/{id}")
    public Result<Store> get(@PathVariable Long id) {
        return Result.success(storeService.getStoreById(id));
    }

    @PostMapping("/create")
    public Result<Store> create(@RequestBody Store store) {
        return Result.success(storeService.createStore(store));
    }

    @PutMapping("/{id}")
    public Result<Store> update(@PathVariable Long id, @RequestBody Store store) {
        return Result.success(storeService.updateStore(id, store));
    }

    @PatchMapping("/{id}/disable")
    public Result<Store> disable(@PathVariable Long id) {
        return Result.success(storeService.disableStore(id));
    }

    @PatchMapping("/{id}/activate")
    public Result<Store> activate(@PathVariable Long id) {
        return Result.success(storeService.activateStore(id));
    }
}