package uk.ac.ncl.csc8019backend.business.menu.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import uk.ac.ncl.csc8019backend.business.menu.entity.Category;
import uk.ac.ncl.csc8019backend.business.menu.entity.Menu;
import uk.ac.ncl.csc8019backend.business.menu.entity.MenuSku;
import uk.ac.ncl.csc8019backend.business.menu.repository.MenuRepository;
import uk.ac.ncl.csc8019backend.business.menu.repository.MenuSkuRepository;
import uk.ac.ncl.csc8019backend.business.menu.service.MenuService;

import java.util.List;

@Service
@AllArgsConstructor
public class MenuServiceImpl implements MenuService {
    private final MenuRepository menuRepository;
    private final MenuSkuRepository menuSkuRepository;

    @Override
    public List<Menu> getAllMenus() {
        return menuRepository.findAll(); // Returns all entities.
    }

    @Override
    public Menu createMenu(Menu menu) {
        return menuRepository.save(menu); // Saves the given entity.
    }

    @Override
    public void deleteMenu(Long id) {
        menuRepository.deleteById(id);
    }

    @Override
    public Menu patchMenu(Long id, Menu menu) {
        Menu existing = menuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu not found"));

        if (menu.getName() != null) {
            existing.setName(menu.getName());
        }
        if (menu.getCategory() != null) {
            existing.setCategory(menu.getCategory());
        }
        return menuRepository.save(existing);
    }

    @Override
    public MenuSku patchMenuSku(Long id, MenuSku sku) {

        MenuSku existing = menuSkuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("SKU not found"));

        if (sku.getSize() != null) {
            existing.setSize(sku.getSize());
        }

        if (sku.getPrice() != null) {
            existing.setPrice(sku.getPrice());
        }

        if (sku.getStock() != null) {
            existing.setStock(sku.getStock());
        }

        if (sku.getIsAvailable() != null) {
            existing.setIsAvailable(sku.getIsAvailable());
        }

        return menuSkuRepository.save(existing);
    }

    @Override
    public List<Menu> getMenuByStoreId(Long storeId) {
        return menuRepository.findByStoreId(storeId);
    }

    @Override
    public List<Menu> getMenuByName(Long storeId, String name) {
        return menuRepository.findByStoreIdAndName(storeId, name);
    }

    @Override
    public List<Menu> getMenuByCategory(Long storeId, Category category) {
        return menuRepository.findByStoreIdAndCategory(storeId, category);
    }

    @Override
    public List<Menu> getMenuByNameAndCategory(Long storeId, String name, Category category) {
        return menuRepository.findByStoreIdAndNameAndCategory(storeId, name, category);
    }

}
