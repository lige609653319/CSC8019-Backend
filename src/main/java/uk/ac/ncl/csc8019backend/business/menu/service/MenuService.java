package uk.ac.ncl.csc8019backend.business.menu.service;

import org.springframework.stereotype.Service;
import uk.ac.ncl.csc8019backend.business.menu.entity.Category;
import uk.ac.ncl.csc8019backend.business.menu.entity.Menu;

import java.util.List;

@Service
public interface MenuService {
    List<Menu> getAllMenus();

    Menu createMenu(Menu menu);

    void deleteMenu(Long id);

    Menu patchMenu(Long id, Menu menu);

    List<Menu> getMenuByName(String name);

    List<Menu> getMenuByCategory(Category category);

    List<Menu> getMenuByNameAndCategory(String name, Category category);
}
