package uk.ac.ncl.csc8019backend.business.menu.controller;

import org.springframework.web.bind.annotation.*;
import uk.ac.ncl.csc8019backend.business.demo.entity.Demo;
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

//    @PostMapping("/create")
//    public Menu create(@RequestBody Menu menu) {
//        return menuRepository.save(menu);

    @PostMapping("/create")
    public Result<Menu> create(@RequestBody Menu menu) {
        return Result.success(menuService.createMenu(menu));
        //调用服务接口里的创建menu方法，而这个方法又是在对应的impl类里实现的，而impl里的实现又是根据repository里的方法
    }


}
