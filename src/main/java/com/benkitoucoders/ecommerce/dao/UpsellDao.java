package com.benkitoucoders.ecommerce.dao;


import com.benkitoucoders.ecommerce.entities.Upsell;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UpsellDao extends JpaRepository<Upsell, Long>
{
    Page<Upsell> findByProductId(Long productId, Pageable pageable);
    Page<Upsell> findByPackageId(Long packageId, Pageable pageable);

}
