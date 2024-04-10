package com.benkitoucoders.ecommerce.dao;

import com.benkitoucoders.ecommerce.entities.Supplier;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SupplierDao extends JpaRepository<Supplier, Long> {
    @Query("SELECT s FROM Supplier s WHERE (:id IS NULL OR s.id = :id) And (:name IS NULL OR s.name = :name) AND (:email IS NULL OR s.email = :email) AND (:phoneNumber IS NULL OR s.phoneNumber = :phoneNumber)")
    List<Supplier> findByCriteria(@Param("id") Long id,@Param("name") String name, @Param("email") String email, @Param("phoneNumber") String phoneNumber, Pageable pageable);

}
