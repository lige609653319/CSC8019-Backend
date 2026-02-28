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
        return menuRepository.findAll();
    }

    @Override
    public Menu createMenu(Menu menu) {
        return menuRepository.save(menu);
    }
}
