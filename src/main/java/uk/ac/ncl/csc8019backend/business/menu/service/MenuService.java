package uk.ac.ncl.csc8019backend.business.menu.service;

import org.springframework.stereotype.Service;
import uk.ac.ncl.csc8019backend.business.menu.entity.Category;
import uk.ac.ncl.csc8019backend.business.menu.entity.Menu;
import uk.ac.ncl.csc8019backend.business.menu.entity.MenuSku;

import java.util.List;

@Service
public interface MenuService {
    List<Menu> getAllMenus();

    Menu createMenu(Menu menu);

    void deleteMenu(Long id);

    Menu patchMenu(Long id, Menu menu);

    MenuSku patchMenuSku(Long id, MenuSku sku);

    List<Menu> getMenuByStoreId(Long storeId);

    List<Menu> getMenuByName(Long storeId, String name);

    List<Menu> getMenuByCategory(Long storeId, Category category);

    List<Menu> getMenuByNameAndCategory(Long storeId, String name, Category category);
}
