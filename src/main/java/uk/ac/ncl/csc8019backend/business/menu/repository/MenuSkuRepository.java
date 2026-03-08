package uk.ac.ncl.csc8019backend.business.menu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.ac.ncl.csc8019backend.business.menu.entity.MenuSku;

public interface MenuSkuRepository extends JpaRepository<MenuSku, Long> {
}