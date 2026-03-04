package uk.ac.ncl.csc8019backend.business.menu.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping("/create")
    public Result<Menu> create(@Valid @RequestBody Menu menu) { //@Valid: check validation
        System.out.println(menu.getId() + " " + menu.getName() + " " + menu.getCategory() + " " + menu.getRegularPrice() + " " + menu.getLargePrice());
        return Result.success(menuService.createMenu(menu));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        menuService.deleteMenu(id);
        return Result.success(null);
    }

    //Partial update
    @PatchMapping("/{id}")
    public Result<Menu> patch(@PathVariable Long id, @RequestBody Menu menu) {
        return Result.success(menuService.patchMenu(id, menu));
    }
}
