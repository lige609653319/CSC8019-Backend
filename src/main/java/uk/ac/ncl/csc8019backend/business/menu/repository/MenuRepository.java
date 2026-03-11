package uk.ac.ncl.csc8019backend.business.menu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.ac.ncl.csc8019backend.business.menu.entity.Category;
import uk.ac.ncl.csc8019backend.business.menu.entity.Menu;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findByStoreId(Long storeId);

    List<Menu> findByStoreIdAndName(Long storeId, String name);

    List<Menu> findByStoreIdAndCategory(Long storeId, Category category);

    List<Menu> findByStoreIdAndNameAndCategory(Long storeId, String name, Category category);
}
