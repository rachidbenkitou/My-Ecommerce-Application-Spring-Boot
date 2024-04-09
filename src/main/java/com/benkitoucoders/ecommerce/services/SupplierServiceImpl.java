package com.benkitoucoders.ecommerce.services;

import com.benkitoucoders.ecommerce.dao.SupplierDao;
import com.benkitoucoders.ecommerce.dtos.SupplierDto;
import com.benkitoucoders.ecommerce.exceptions.EntityNotFoundException;
import com.benkitoucoders.ecommerce.mappers.SupplierMapper;
import com.benkitoucoders.ecommerce.services.inter.SupplierService;
import com.benkitoucoders.ecommerce.entities.Supplier;
import com.benkitoucoders.ecommerce.dtos.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService {
    private final SupplierDao supplierDao;
    private final SupplierMapper supplierMapper;

    /**
     * Retrieves a list of suppliers based on the specified criteria.
     *
     * @param supplierId   The ID of the supplier to filter by.
     * @param name         The name of the supplier to filter by.
     * @param email        The emailservice of the supplier to filter by.
     * @param phoneNumber  The phone number of the supplier to filter by.
     * @param pageable     Pagination information.
     * @return             A list of SupplierDto objects representing the suppliers that match the criteria.
     * @throws RuntimeException If an error occurs while retrieving suppliers.
     */
    @Override
    public List<SupplierDto> getSuppliers(Long supplierId, String name, String email, String phoneNumber, Pageable pageable) {
        try {
            // Assuming there's a method in SupplierDao to find suppliers with the given criteria
            List<Supplier> suppliers = supplierDao.findByCriteria(supplierId, name, email, phoneNumber, pageable);
            return supplierMapper.modelsToDtos(suppliers);
        } catch (Exception e) {
            log.error("Error retrieving suppliers", e);
            throw new RuntimeException("Error retrieving suppliers", e);
        }
    }

    /**
     * Retrieves a supplier by its ID.
     *
     * @param id The ID of the supplier to retrieve.
     * @return   A SupplierDto object representing the supplier with the specified ID.
     * @throws EntityNotFoundException If the supplier with the specified ID is not found.
     */
    @Override
    public SupplierDto getSupplierById(Long id) {
        log.debug("Fetching supplier with id : {}",id);
        return supplierDao.findById(id)
                .map(supplier -> {
                    log.info("supplier found with id: {}",id);
                    return new SupplierDto(supplier.getId(),supplier.getName(),supplier.getEmail(),supplier.getAddress(),supplier.getPhoneNumber(),supplier.getDateCreation());
                })
                .orElseThrow(() -> {
            log.error("supplier not found with id:{}",id);
            return new EntityNotFoundException(String.format("the supplier with the id %d is not found",id));
        });
    }
    /**
     * Adds a new supplier.
     *
     * @param supplierDto The SupplierDto object representing the new supplier to add.
     * @return            A SupplierDto object representing the added supplier.
     * @throws IOException If an I/O error occurs while adding the supplier.
     */
    @Override
    public SupplierDto addSupplier(SupplierDto supplierDto) throws IOException {
        try {
            Supplier supplier = supplierMapper.dtoToModel(supplierDto);
            Supplier savedSupplier = supplierDao.save(supplier);
            log.info("Added new supplier with id: {}", savedSupplier.getId());
            return supplierMapper.modelToDto(savedSupplier);
        } catch (Exception e) {
            log.error("Error adding new supplier", e);
            throw new RuntimeException("Error adding new supplier", e);
        }
    }
    /**
     * Updates an existing supplier.
     *
     * @param id          The ID of the supplier to update.
     * @param supplierDto The SupplierDto object representing the updated supplier details.
     * @return            A SupplierDto object representing the updated supplier.
     */
    @Override
    public SupplierDto updateSupplier(Long id, SupplierDto supplierDto)
    {
        try {
            Supplier existingSupplier = supplierDao.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Supplier not found with id: " + id));
            supplierMapper.updateModelWithDto(supplierDto, existingSupplier);
            Supplier updatedSupplier = supplierDao.save(existingSupplier);
            log.info("Updated supplier with id: {}", updatedSupplier.getId());
            return supplierMapper.modelToDto(updatedSupplier);
        } catch (EntityNotFoundException enfe) {
            log.error("Supplier not found with id: {}", id, enfe);
            throw enfe;
        } catch (Exception e) {
            log.error("Error updating supplier with id: {}", id, e);
            throw new RuntimeException("Error updating supplier with id: " + id, e);
        }
    }
    /**
     * Deletes a supplier by its ID.
     *
     * @param id The ID of the supplier to delete.
     * @return   A ResponseDto object indicating the result of the deletion operation.
     */
    @Override
    public ResponseDto deleteSupplierById(Long id)
    {
        try {
            Supplier supplier = supplierDao.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Supplier not found with id: " + id));
            supplierDao.delete(supplier);
            log.info("Deleted supplier with id: {}", id);
            return new ResponseDto("Supplier successfully deleted", true);
        } catch (EntityNotFoundException enfe) {
            log.error("Supplier not found with id: {}", id, enfe);
            throw enfe;
        } catch (Exception e) {
            log.error("Error deleting supplier with id: {}", id, e);
            throw new RuntimeException("Error deleting supplier with id: " + id, e);
        }
    }
}
