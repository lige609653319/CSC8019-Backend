package uk.ac.ncl.csc8019backend.business.menu.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import uk.ac.ncl.csc8019backend.business.menu.entity.Category;
import uk.ac.ncl.csc8019backend.business.menu.entity.Menu;
import uk.ac.ncl.csc8019backend.business.menu.entity.MenuSku;
import uk.ac.ncl.csc8019backend.business.menu.service.MenuService;
import uk.ac.ncl.csc8019backend.system.common.Result;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/menu")
@AllArgsConstructor
public class MenuController {
    private final MenuService menuService;

    @GetMapping("/list")
    public Result<List<Menu>> list() {
        return Result.success(menuService.getAllMenus());
    }

    // @RequestBody: read parameters from the request body, and convert JSON into Object
    @PostMapping("/create")
    public Result<Menu> create(@Valid @RequestBody Menu menu) { // @Valid: check validation
        if (menu.getSkus() != null) {
            for (MenuSku sku : menu.getSkus()) {
                sku.setMenu(menu);
            }
        }
        return Result.success(menuService.createMenu(menu));
    }

    // @PathVariable: read parameters from URL path
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        menuService.deleteMenu(id);
        return Result.success(null);
    }

    // Partial update
    @PatchMapping("/{id}")
    public Result<Menu> patch(@PathVariable Long id, @RequestBody Menu menu) {
        return Result.success(menuService.patchMenu(id, menu));
    }

    //Update sku
    @PatchMapping("/sku/{id}")
    public Result<MenuSku> patchSku(@PathVariable Long id, @RequestBody MenuSku sku) {
        return Result.success(menuService.patchMenuSku(id, sku));
    }

    // @RequestParam: read parameters from query parameters (not from the path)
    @GetMapping("/search")
    public Result<List<Menu>> get(@RequestParam(required = false) String name,
                                  @RequestParam(required = false) Category category,
                                  @RequestParam(required = false) Long storeId) {

        // Default: display menus for storeId = 1 (Long)
        if (storeId == null) {
            storeId = 1L;
        }

        if (name != null && !name.trim().isEmpty() && category != null) {
            return Result.success(menuService.getMenuByNameAndCategory(storeId, name, category));
        }

        if (name != null && !name.trim().isEmpty()) {
            return Result.success(menuService.getMenuByName(storeId, name));
        }

        if (category != null) {
            return Result.success(menuService.getMenuByCategory(storeId, category));
        }

        return Result.success(menuService.getMenuByStoreId(storeId));
    }


}
