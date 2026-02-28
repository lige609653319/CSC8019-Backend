package uk.ac.ncl.csc8019backend.business.menu.controller;

import org.springframework.web.bind.annotation.*;
import uk.ac.ncl.csc8019backend.business.menu.entity.Menu;
import uk.ac.ncl.csc8019backend.business.menu.service.MenuService;
import uk.ac.ncl.csc8019backend.system.common.Result;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
public class MenuController {
    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping("/list")
    public Result<List<Menu>> list() {
        return Result.success(menuService.getAllMenus());
    }


}
