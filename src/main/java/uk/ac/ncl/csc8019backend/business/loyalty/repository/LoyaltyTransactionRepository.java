package uk.ac.ncl.csc8019backend.business.loyalty.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.ac.ncl.csc8019backend.business.loyalty.entity.LoyaltyTransaction;

@Repository
public interface LoyaltyTransactionRepository extends JpaRepository<LoyaltyTransaction, Long> {
    Page<LoyaltyTransaction> findByUsernameOrderByCreatedAtDesc(String username, Pageable pageable);
}