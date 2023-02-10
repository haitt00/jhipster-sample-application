package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.VRequestFieldDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.VRequestField}.
 */
public interface VRequestFieldService {
    /**
     * Save a vRequestField.
     *
     * @param vRequestFieldDTO the entity to save.
     * @return the persisted entity.
     */
    VRequestFieldDTO save(VRequestFieldDTO vRequestFieldDTO);

    /**
     * Updates a vRequestField.
     *
     * @param vRequestFieldDTO the entity to update.
     * @return the persisted entity.
     */
    VRequestFieldDTO update(VRequestFieldDTO vRequestFieldDTO);

    /**
     * Partially updates a vRequestField.
     *
     * @param vRequestFieldDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<VRequestFieldDTO> partialUpdate(VRequestFieldDTO vRequestFieldDTO);

    /**
     * Get all the vRequestFields.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<VRequestFieldDTO> findAll(Pageable pageable);

    /**
     * Get the "id" vRequestField.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<VRequestFieldDTO> findOne(Long id);

    /**
     * Delete the "id" vRequestField.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
