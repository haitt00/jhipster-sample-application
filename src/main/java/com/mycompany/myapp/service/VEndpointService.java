package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.VEndpointDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.VEndpoint}.
 */
public interface VEndpointService {
    /**
     * Save a vEndpoint.
     *
     * @param vEndpointDTO the entity to save.
     * @return the persisted entity.
     */
    VEndpointDTO save(VEndpointDTO vEndpointDTO);

    /**
     * Updates a vEndpoint.
     *
     * @param vEndpointDTO the entity to update.
     * @return the persisted entity.
     */
    VEndpointDTO update(VEndpointDTO vEndpointDTO);

    /**
     * Partially updates a vEndpoint.
     *
     * @param vEndpointDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<VEndpointDTO> partialUpdate(VEndpointDTO vEndpointDTO);

    /**
     * Get all the vEndpoints.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<VEndpointDTO> findAll(Pageable pageable);

    /**
     * Get the "id" vEndpoint.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<VEndpointDTO> findOne(Long id);

    /**
     * Delete the "id" vEndpoint.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
