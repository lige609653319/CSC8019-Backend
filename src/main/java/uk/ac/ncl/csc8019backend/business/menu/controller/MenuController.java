package uk.ac.ncl.csc8019backend.business.menu.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import uk.ac.ncl.csc8019backend.business.menu.entity.Menu;
import uk.ac.ncl.csc8019backend.business.menu.service.MenuService;
import uk.ac.ncl.csc8019backend.system.common.Result;

import java.util.List;

@RestController
@RequestMapping("/api/menu") //指定请求路径
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
        //三层架构 调用服务接口里的创建menu方法，而这个方法又是在对应的impl类里实现的，而impl里的实现又是根据repository里的方法
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
