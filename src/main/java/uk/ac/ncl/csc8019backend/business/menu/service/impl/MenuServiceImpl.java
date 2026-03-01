package uk.ac.ncl.csc8019backend.business.menu.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import uk.ac.ncl.csc8019backend.business.menu.entity.Menu;
import uk.ac.ncl.csc8019backend.business.menu.repository.MenuRepository;
import uk.ac.ncl.csc8019backend.business.menu.service.MenuService;

import java.util.List;

@Service
@AllArgsConstructor //有参构造
public class MenuServiceImpl implements MenuService {
    private final MenuRepository menuRepository;

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
        if (menu.getRegularPrice() != null) {
            existing.setRegularPrice(menu.getRegularPrice());
        }
        if (menu.getLargePrice() != null) {
            existing.setLargePrice(menu.getLargePrice());
        }
        if (menu.getStock() != null) {
            existing.setStock(menu.getStock());
        }
        if (menu.getIsAvailable() != null) {
            existing.setIsAvailable(menu.getIsAvailable());
        }

        return menuRepository.save(existing);
    }


}
