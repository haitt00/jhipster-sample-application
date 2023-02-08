package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.CreditCard;
import com.mycompany.myapp.repository.CreditCardRepository;
import com.mycompany.myapp.service.dto.CreditCardDTO;
import com.mycompany.myapp.service.mapper.CreditCardMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CreditCardResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CreditCardResourceIT {

    private static final String DEFAULT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_BANK = "AAAAAAAAAA";
    private static final String UPDATED_BANK = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/credit-cards";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Autowired
    private CreditCardMapper creditCardMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCreditCardMockMvc;

    private CreditCard creditCard;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CreditCard createEntity(EntityManager em) {
        CreditCard creditCard = new CreditCard().number(DEFAULT_NUMBER).bank(DEFAULT_BANK);
        return creditCard;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CreditCard createUpdatedEntity(EntityManager em) {
        CreditCard creditCard = new CreditCard().number(UPDATED_NUMBER).bank(UPDATED_BANK);
        return creditCard;
    }

    @BeforeEach
    public void initTest() {
        creditCard = createEntity(em);
    }

    @Test
    @Transactional
    void createCreditCard() throws Exception {
        int databaseSizeBeforeCreate = creditCardRepository.findAll().size();
        // Create the CreditCard
        CreditCardDTO creditCardDTO = creditCardMapper.toDto(creditCard);
        restCreditCardMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(creditCardDTO)))
            .andExpect(status().isCreated());

        // Validate the CreditCard in the database
        List<CreditCard> creditCardList = creditCardRepository.findAll();
        assertThat(creditCardList).hasSize(databaseSizeBeforeCreate + 1);
        CreditCard testCreditCard = creditCardList.get(creditCardList.size() - 1);
        assertThat(testCreditCard.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testCreditCard.getBank()).isEqualTo(DEFAULT_BANK);
    }

    @Test
    @Transactional
    void createCreditCardWithExistingId() throws Exception {
        // Create the CreditCard with an existing ID
        creditCard.setId(1L);
        CreditCardDTO creditCardDTO = creditCardMapper.toDto(creditCard);

        int databaseSizeBeforeCreate = creditCardRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCreditCardMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(creditCardDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CreditCard in the database
        List<CreditCard> creditCardList = creditCardRepository.findAll();
        assertThat(creditCardList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCreditCards() throws Exception {
        // Initialize the database
        creditCardRepository.saveAndFlush(creditCard);

        // Get all the creditCardList
        restCreditCardMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(creditCard.getId().intValue())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER)))
            .andExpect(jsonPath("$.[*].bank").value(hasItem(DEFAULT_BANK)));
    }

    @Test
    @Transactional
    void getCreditCard() throws Exception {
        // Initialize the database
        creditCardRepository.saveAndFlush(creditCard);

        // Get the creditCard
        restCreditCardMockMvc
            .perform(get(ENTITY_API_URL_ID, creditCard.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(creditCard.getId().intValue()))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER))
            .andExpect(jsonPath("$.bank").value(DEFAULT_BANK));
    }

    @Test
    @Transactional
    void getNonExistingCreditCard() throws Exception {
        // Get the creditCard
        restCreditCardMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCreditCard() throws Exception {
        // Initialize the database
        creditCardRepository.saveAndFlush(creditCard);

        int databaseSizeBeforeUpdate = creditCardRepository.findAll().size();

        // Update the creditCard
        CreditCard updatedCreditCard = creditCardRepository.findById(creditCard.getId()).get();
        // Disconnect from session so that the updates on updatedCreditCard are not directly saved in db
        em.detach(updatedCreditCard);
        updatedCreditCard.number(UPDATED_NUMBER).bank(UPDATED_BANK);
        CreditCardDTO creditCardDTO = creditCardMapper.toDto(updatedCreditCard);

        restCreditCardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, creditCardDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(creditCardDTO))
            )
            .andExpect(status().isOk());

        // Validate the CreditCard in the database
        List<CreditCard> creditCardList = creditCardRepository.findAll();
        assertThat(creditCardList).hasSize(databaseSizeBeforeUpdate);
        CreditCard testCreditCard = creditCardList.get(creditCardList.size() - 1);
        assertThat(testCreditCard.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testCreditCard.getBank()).isEqualTo(UPDATED_BANK);
    }

    @Test
    @Transactional
    void putNonExistingCreditCard() throws Exception {
        int databaseSizeBeforeUpdate = creditCardRepository.findAll().size();
        creditCard.setId(count.incrementAndGet());

        // Create the CreditCard
        CreditCardDTO creditCardDTO = creditCardMapper.toDto(creditCard);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCreditCardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, creditCardDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(creditCardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CreditCard in the database
        List<CreditCard> creditCardList = creditCardRepository.findAll();
        assertThat(creditCardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCreditCard() throws Exception {
        int databaseSizeBeforeUpdate = creditCardRepository.findAll().size();
        creditCard.setId(count.incrementAndGet());

        // Create the CreditCard
        CreditCardDTO creditCardDTO = creditCardMapper.toDto(creditCard);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCreditCardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(creditCardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CreditCard in the database
        List<CreditCard> creditCardList = creditCardRepository.findAll();
        assertThat(creditCardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCreditCard() throws Exception {
        int databaseSizeBeforeUpdate = creditCardRepository.findAll().size();
        creditCard.setId(count.incrementAndGet());

        // Create the CreditCard
        CreditCardDTO creditCardDTO = creditCardMapper.toDto(creditCard);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCreditCardMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(creditCardDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CreditCard in the database
        List<CreditCard> creditCardList = creditCardRepository.findAll();
        assertThat(creditCardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCreditCardWithPatch() throws Exception {
        // Initialize the database
        creditCardRepository.saveAndFlush(creditCard);

        int databaseSizeBeforeUpdate = creditCardRepository.findAll().size();

        // Update the creditCard using partial update
        CreditCard partialUpdatedCreditCard = new CreditCard();
        partialUpdatedCreditCard.setId(creditCard.getId());

        partialUpdatedCreditCard.number(UPDATED_NUMBER);

        restCreditCardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCreditCard.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCreditCard))
            )
            .andExpect(status().isOk());

        // Validate the CreditCard in the database
        List<CreditCard> creditCardList = creditCardRepository.findAll();
        assertThat(creditCardList).hasSize(databaseSizeBeforeUpdate);
        CreditCard testCreditCard = creditCardList.get(creditCardList.size() - 1);
        assertThat(testCreditCard.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testCreditCard.getBank()).isEqualTo(DEFAULT_BANK);
    }

    @Test
    @Transactional
    void fullUpdateCreditCardWithPatch() throws Exception {
        // Initialize the database
        creditCardRepository.saveAndFlush(creditCard);

        int databaseSizeBeforeUpdate = creditCardRepository.findAll().size();

        // Update the creditCard using partial update
        CreditCard partialUpdatedCreditCard = new CreditCard();
        partialUpdatedCreditCard.setId(creditCard.getId());

        partialUpdatedCreditCard.number(UPDATED_NUMBER).bank(UPDATED_BANK);

        restCreditCardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCreditCard.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCreditCard))
            )
            .andExpect(status().isOk());

        // Validate the CreditCard in the database
        List<CreditCard> creditCardList = creditCardRepository.findAll();
        assertThat(creditCardList).hasSize(databaseSizeBeforeUpdate);
        CreditCard testCreditCard = creditCardList.get(creditCardList.size() - 1);
        assertThat(testCreditCard.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testCreditCard.getBank()).isEqualTo(UPDATED_BANK);
    }

    @Test
    @Transactional
    void patchNonExistingCreditCard() throws Exception {
        int databaseSizeBeforeUpdate = creditCardRepository.findAll().size();
        creditCard.setId(count.incrementAndGet());

        // Create the CreditCard
        CreditCardDTO creditCardDTO = creditCardMapper.toDto(creditCard);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCreditCardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, creditCardDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(creditCardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CreditCard in the database
        List<CreditCard> creditCardList = creditCardRepository.findAll();
        assertThat(creditCardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCreditCard() throws Exception {
        int databaseSizeBeforeUpdate = creditCardRepository.findAll().size();
        creditCard.setId(count.incrementAndGet());

        // Create the CreditCard
        CreditCardDTO creditCardDTO = creditCardMapper.toDto(creditCard);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCreditCardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(creditCardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CreditCard in the database
        List<CreditCard> creditCardList = creditCardRepository.findAll();
        assertThat(creditCardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCreditCard() throws Exception {
        int databaseSizeBeforeUpdate = creditCardRepository.findAll().size();
        creditCard.setId(count.incrementAndGet());

        // Create the CreditCard
        CreditCardDTO creditCardDTO = creditCardMapper.toDto(creditCard);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCreditCardMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(creditCardDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CreditCard in the database
        List<CreditCard> creditCardList = creditCardRepository.findAll();
        assertThat(creditCardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCreditCard() throws Exception {
        // Initialize the database
        creditCardRepository.saveAndFlush(creditCard);

        int databaseSizeBeforeDelete = creditCardRepository.findAll().size();

        // Delete the creditCard
        restCreditCardMockMvc
            .perform(delete(ENTITY_API_URL_ID, creditCard.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CreditCard> creditCardList = creditCardRepository.findAll();
        assertThat(creditCardList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
