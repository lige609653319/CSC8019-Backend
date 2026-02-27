package uk.ac.ncl.csc8019backend.business.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.ac.ncl.csc8019backend.business.demo.entity.Demo;

@Repository
public interface DemoRepository extends JpaRepository<Demo, Long> {
}
