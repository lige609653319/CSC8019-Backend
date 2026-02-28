package uk.ac.ncl.csc8019backend.business.menu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.ac.ncl.csc8019backend.business.menu.entity.Menu;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
//    List<Demo> findByName(String name);//暂时不用管
}
