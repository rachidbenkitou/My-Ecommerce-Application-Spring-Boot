package com.benkitoucoders.ecommerce.services;


import com.benkitoucoders.ecommerce.dao.UpsellDao;
import com.benkitoucoders.ecommerce.dtos.ResponseDto;
import com.benkitoucoders.ecommerce.dtos.UpsellDto;
import com.benkitoucoders.ecommerce.entities.Upsell;
import com.benkitoucoders.ecommerce.exceptions.EntityNotFoundException;
import com.benkitoucoders.ecommerce.exceptions.EntityServiceException;
import com.benkitoucoders.ecommerce.mappers.UpsellMapper;
import com.benkitoucoders.ecommerce.services.inter.UpsellService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class UpsellServiceImpl implements UpsellService {

    private final UpsellDao upsellDao;

    private final UpsellMapper upsellMapper;
    /**
     * Retrieves a page of upsells.
     *
     * @param pageable Pagination information.
     * @return A Page object containing a list of Upsell objects.
     */
    @Override
    public Page<Upsell> getUpsells(Pageable pageable) {
        return upsellDao.findAll(pageable);
    }

    @Override
    public Page<UpsellDto> searchUpsellsByProductId(Long productId, Pageable pageable) {
        log.info("Searching for upsells by productId: {}", productId);
        return upsellDao.findByProductId(productId, pageable)
                .map(upsellMapper::modelToDto);
    }

    @Override
    public Page<UpsellDto> searchUpsellsByPackageId(Long packageId, Pageable pageable) {
        log.info("Searching for upsells by packageId: {}", packageId);
        return upsellDao.findByPackageId(packageId, pageable)
                .map(upsellMapper::modelToDto);
    }

    /**
     * Retrieves an upsell by its ID.
     *
     * @param id The ID of the upsell to retrieve.
     * @return A UpsellDto object representing the upsell with the specified ID.
     * @throws EntityNotFoundException If the upsell with the specified ID is not found.
     */
    @Override
    public UpsellDto getUpsellById(Long id) {
        log.info("Fetching upsell getUpsellById with id: {} execution started.", id);
        return upsellDao.findById(id)
                .map(upsell -> {
                    log.info("Upsell found with id: {}", id);
                    return new UpsellDto(upsell.getId(), upsell.getTitle(), upsell.getDescription(), upsell.getBottomTitle());
                })
                .orElseThrow(() -> {
                    log.error("Upsell not found with id: {}", id);
                    return new EntityNotFoundException(String.format("The upsell with the id %d is not found.", id));
                });
    }
    /**
     * Adds a new upsell.
     *
     * @param upsellDto The UpsellDto object representing the new upsell to add.
     * @return A UpsellDto object representing the added upsell.
     * @throws IOException If an I/O error occurs while adding the upsell.
     */
    @Override
    public UpsellDto addUpsell(UpsellDto upsellDto) throws IOException {
        log.info("addUpsell: Adding new upsell execution started.");
        try {
            log.debug("Adding new upsell: {}", upsellDto);
            upsellDto.setId(null);
            Upsell savedUpsell = upsellDao.save(upsellMapper.dtoToModel(upsellDto));
            log.debug("Upsell successfully added with id: {}", savedUpsell.getId());
            return upsellMapper.modelToDto(savedUpsell);
        } catch (Exception e) {
            log.error("An error occurred while storing the upsell: {}", upsellDto, e);
            throw new EntityServiceException("An error occurred while storing the upsell.", e);
        }
    }

    /**
     * Updates an existing upsell.
     *
     * @param id The ID of the upsell to update.
     * @param upsellDto The UpsellDto object representing the updated upsell details.
     * @return A UpsellDto object representing the updated upsell.
     * @throws EntityServiceException If an error occurs while updating the upsell.
     */
    @Override
    public UpsellDto updateUpsell(Long id, UpsellDto upsellDto) {

        log.info("Updating upsell with id: {}", id);
        try {
            UpsellDto oldUpsellDto = getUpsellById(id);
            Upsell upsellToUpdate = upsellMapper.dtoToModel(upsellDto);
            upsellToUpdate.setId(oldUpsellDto.getId()); // Preserve the original ID

            Upsell updatedUpsell = upsellDao.save(upsellToUpdate);
            log.debug("Upsell successfully updated for id: {}", id);
            return upsellMapper.modelToDto(updatedUpsell);
        } catch (Exception e) {
            log.error("An error occurred while updating the upsell with id: {}", id, e);
            throw new EntityServiceException("An error occurred while updating the upsell.", e);
        }
    }
    /**
     * Deletes an upsell by its ID.
     *
     * @param id The ID of the upsell to delete.
     * @return A ResponseDto object indicating the result of the deletion operation.
     * @throws EntityServiceException If an error occurs while deleting the upsell.
     */
    @Override
    public ResponseDto deleteUpsellById(Long id) {
        log.info("Deleting upsell with id: {}", id);
        try {
            if (getUpsellById(id) != null) {
                upsellDao.deleteById(id);
                log.debug("Upsell successfully deleted with id: {}", id);
                return ResponseDto.builder()
                        .message("Upsell successfully deleted.")
                        .build();
            } else {
                throw new EntityServiceException("The id is not found");
            }
        } catch (Exception e) {
            log.error("An error occurred while deleting the upsell with id: {}", id, e);
            throw new EntityServiceException("An error occurred while deleting the upsell.", e);
        }
    }


}
