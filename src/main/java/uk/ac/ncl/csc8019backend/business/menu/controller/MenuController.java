package uk.ac.ncl.csc8019backend.business.menu.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import uk.ac.ncl.csc8019backend.business.menu.entity.Category;
import uk.ac.ncl.csc8019backend.business.menu.entity.Menu;
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
        System.out.println(menu.getId() + " " + menu.getName() + " " + menu.getCategory() + " " + menu.getRegularPrice() + " " + menu.getLargePrice());
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

    // @RequestParam: read parameters from query parameters (not from the path)
    @GetMapping("/search")
    public Result<List<Menu>> get(@RequestParam(required = false) String name,
                                  @RequestParam(required = false) Category category) {
        if (name != null && category != null) {
            return Result.success(menuService.getMenuByNameAndCategory(name, category));
        }
        if (name != null) {
            return Result.success(menuService.getMenuByName(name));
        }
        if (category != null) {
            return Result.success(menuService.getMenuByCategory(category));
        }
        return Result.success(menuService.getAllMenus());
    }


}
