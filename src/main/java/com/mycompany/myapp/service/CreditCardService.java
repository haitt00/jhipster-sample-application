package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.CreditCardDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.CreditCard}.
 */
public interface CreditCardService {
    /**
     * Save a creditCard.
     *
     * @param creditCardDTO the entity to save.
     * @return the persisted entity.
     */
    CreditCardDTO save(CreditCardDTO creditCardDTO);

    /**
     * Updates a creditCard.
     *
     * @param creditCardDTO the entity to update.
     * @return the persisted entity.
     */
    CreditCardDTO update(CreditCardDTO creditCardDTO);

    /**
     * Partially updates a creditCard.
     *
     * @param creditCardDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CreditCardDTO> partialUpdate(CreditCardDTO creditCardDTO);

    /**
     * Get all the creditCards.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CreditCardDTO> findAll(Pageable pageable);

    /**
     * Get the "id" creditCard.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CreditCardDTO> findOne(Long id);

    /**
     * Delete the "id" creditCard.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
