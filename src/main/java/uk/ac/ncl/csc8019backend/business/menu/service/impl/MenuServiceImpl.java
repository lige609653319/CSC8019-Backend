package uk.ac.ncl.csc8019backend.business.menu.service.impl;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import uk.ac.ncl.csc8019backend.business.menu.entity.Menu;
import uk.ac.ncl.csc8019backend.business.menu.repository.MenuRepository;
import uk.ac.ncl.csc8019backend.business.menu.service.MenuService;

import java.util.List;
//@NoArgsConstructor //无参构造函数
@AllArgsConstructor //有参构造
public class MenuServiceImpl implements MenuService {
    private final MenuRepository menuRepository;
    @Override
    public List<Menu> getAllMenus() {
        return menuRepository.findAll();
    }
}
