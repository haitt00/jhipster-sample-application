package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.VRequestField;
import com.mycompany.myapp.repository.VRequestFieldRepository;
import com.mycompany.myapp.service.dto.VRequestFieldDTO;
import com.mycompany.myapp.service.mapper.VRequestFieldMapper;
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
 * Integration tests for the {@link VRequestFieldResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VRequestFieldResourceIT {

    private static final Long DEFAULT_ENDPOINT_ID = 1L;
    private static final Long UPDATED_ENDPOINT_ID = 2L;

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/v-request-fields";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VRequestFieldRepository vRequestFieldRepository;

    @Autowired
    private VRequestFieldMapper vRequestFieldMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVRequestFieldMockMvc;

    private VRequestField vRequestField;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VRequestField createEntity(EntityManager em) {
        VRequestField vRequestField = new VRequestField().endpointId(DEFAULT_ENDPOINT_ID).code(DEFAULT_CODE).name(DEFAULT_NAME);
        return vRequestField;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VRequestField createUpdatedEntity(EntityManager em) {
        VRequestField vRequestField = new VRequestField().endpointId(UPDATED_ENDPOINT_ID).code(UPDATED_CODE).name(UPDATED_NAME);
        return vRequestField;
    }

    @BeforeEach
    public void initTest() {
        vRequestField = createEntity(em);
    }

    @Test
    @Transactional
    void createVRequestField() throws Exception {
        int databaseSizeBeforeCreate = vRequestFieldRepository.findAll().size();
        // Create the VRequestField
        VRequestFieldDTO vRequestFieldDTO = vRequestFieldMapper.toDto(vRequestField);
        restVRequestFieldMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vRequestFieldDTO))
            )
            .andExpect(status().isCreated());

        // Validate the VRequestField in the database
        List<VRequestField> vRequestFieldList = vRequestFieldRepository.findAll();
        assertThat(vRequestFieldList).hasSize(databaseSizeBeforeCreate + 1);
        VRequestField testVRequestField = vRequestFieldList.get(vRequestFieldList.size() - 1);
        assertThat(testVRequestField.getEndpointId()).isEqualTo(DEFAULT_ENDPOINT_ID);
        assertThat(testVRequestField.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testVRequestField.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createVRequestFieldWithExistingId() throws Exception {
        // Create the VRequestField with an existing ID
        vRequestField.setId(1L);
        VRequestFieldDTO vRequestFieldDTO = vRequestFieldMapper.toDto(vRequestField);

        int databaseSizeBeforeCreate = vRequestFieldRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVRequestFieldMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vRequestFieldDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VRequestField in the database
        List<VRequestField> vRequestFieldList = vRequestFieldRepository.findAll();
        assertThat(vRequestFieldList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllVRequestFields() throws Exception {
        // Initialize the database
        vRequestFieldRepository.saveAndFlush(vRequestField);

        // Get all the vRequestFieldList
        restVRequestFieldMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vRequestField.getId().intValue())))
            .andExpect(jsonPath("$.[*].endpointId").value(hasItem(DEFAULT_ENDPOINT_ID.intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getVRequestField() throws Exception {
        // Initialize the database
        vRequestFieldRepository.saveAndFlush(vRequestField);

        // Get the vRequestField
        restVRequestFieldMockMvc
            .perform(get(ENTITY_API_URL_ID, vRequestField.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vRequestField.getId().intValue()))
            .andExpect(jsonPath("$.endpointId").value(DEFAULT_ENDPOINT_ID.intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingVRequestField() throws Exception {
        // Get the vRequestField
        restVRequestFieldMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVRequestField() throws Exception {
        // Initialize the database
        vRequestFieldRepository.saveAndFlush(vRequestField);

        int databaseSizeBeforeUpdate = vRequestFieldRepository.findAll().size();

        // Update the vRequestField
        VRequestField updatedVRequestField = vRequestFieldRepository.findById(vRequestField.getId()).get();
        // Disconnect from session so that the updates on updatedVRequestField are not directly saved in db
        em.detach(updatedVRequestField);
        updatedVRequestField.endpointId(UPDATED_ENDPOINT_ID).code(UPDATED_CODE).name(UPDATED_NAME);
        VRequestFieldDTO vRequestFieldDTO = vRequestFieldMapper.toDto(updatedVRequestField);

        restVRequestFieldMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vRequestFieldDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vRequestFieldDTO))
            )
            .andExpect(status().isOk());

        // Validate the VRequestField in the database
        List<VRequestField> vRequestFieldList = vRequestFieldRepository.findAll();
        assertThat(vRequestFieldList).hasSize(databaseSizeBeforeUpdate);
        VRequestField testVRequestField = vRequestFieldList.get(vRequestFieldList.size() - 1);
        assertThat(testVRequestField.getEndpointId()).isEqualTo(UPDATED_ENDPOINT_ID);
        assertThat(testVRequestField.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testVRequestField.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingVRequestField() throws Exception {
        int databaseSizeBeforeUpdate = vRequestFieldRepository.findAll().size();
        vRequestField.setId(count.incrementAndGet());

        // Create the VRequestField
        VRequestFieldDTO vRequestFieldDTO = vRequestFieldMapper.toDto(vRequestField);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVRequestFieldMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vRequestFieldDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vRequestFieldDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VRequestField in the database
        List<VRequestField> vRequestFieldList = vRequestFieldRepository.findAll();
        assertThat(vRequestFieldList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVRequestField() throws Exception {
        int databaseSizeBeforeUpdate = vRequestFieldRepository.findAll().size();
        vRequestField.setId(count.incrementAndGet());

        // Create the VRequestField
        VRequestFieldDTO vRequestFieldDTO = vRequestFieldMapper.toDto(vRequestField);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVRequestFieldMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vRequestFieldDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VRequestField in the database
        List<VRequestField> vRequestFieldList = vRequestFieldRepository.findAll();
        assertThat(vRequestFieldList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVRequestField() throws Exception {
        int databaseSizeBeforeUpdate = vRequestFieldRepository.findAll().size();
        vRequestField.setId(count.incrementAndGet());

        // Create the VRequestField
        VRequestFieldDTO vRequestFieldDTO = vRequestFieldMapper.toDto(vRequestField);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVRequestFieldMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vRequestFieldDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the VRequestField in the database
        List<VRequestField> vRequestFieldList = vRequestFieldRepository.findAll();
        assertThat(vRequestFieldList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVRequestFieldWithPatch() throws Exception {
        // Initialize the database
        vRequestFieldRepository.saveAndFlush(vRequestField);

        int databaseSizeBeforeUpdate = vRequestFieldRepository.findAll().size();

        // Update the vRequestField using partial update
        VRequestField partialUpdatedVRequestField = new VRequestField();
        partialUpdatedVRequestField.setId(vRequestField.getId());

        partialUpdatedVRequestField.code(UPDATED_CODE).name(UPDATED_NAME);

        restVRequestFieldMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVRequestField.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVRequestField))
            )
            .andExpect(status().isOk());

        // Validate the VRequestField in the database
        List<VRequestField> vRequestFieldList = vRequestFieldRepository.findAll();
        assertThat(vRequestFieldList).hasSize(databaseSizeBeforeUpdate);
        VRequestField testVRequestField = vRequestFieldList.get(vRequestFieldList.size() - 1);
        assertThat(testVRequestField.getEndpointId()).isEqualTo(DEFAULT_ENDPOINT_ID);
        assertThat(testVRequestField.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testVRequestField.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateVRequestFieldWithPatch() throws Exception {
        // Initialize the database
        vRequestFieldRepository.saveAndFlush(vRequestField);

        int databaseSizeBeforeUpdate = vRequestFieldRepository.findAll().size();

        // Update the vRequestField using partial update
        VRequestField partialUpdatedVRequestField = new VRequestField();
        partialUpdatedVRequestField.setId(vRequestField.getId());

        partialUpdatedVRequestField.endpointId(UPDATED_ENDPOINT_ID).code(UPDATED_CODE).name(UPDATED_NAME);

        restVRequestFieldMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVRequestField.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVRequestField))
            )
            .andExpect(status().isOk());

        // Validate the VRequestField in the database
        List<VRequestField> vRequestFieldList = vRequestFieldRepository.findAll();
        assertThat(vRequestFieldList).hasSize(databaseSizeBeforeUpdate);
        VRequestField testVRequestField = vRequestFieldList.get(vRequestFieldList.size() - 1);
        assertThat(testVRequestField.getEndpointId()).isEqualTo(UPDATED_ENDPOINT_ID);
        assertThat(testVRequestField.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testVRequestField.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingVRequestField() throws Exception {
        int databaseSizeBeforeUpdate = vRequestFieldRepository.findAll().size();
        vRequestField.setId(count.incrementAndGet());

        // Create the VRequestField
        VRequestFieldDTO vRequestFieldDTO = vRequestFieldMapper.toDto(vRequestField);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVRequestFieldMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vRequestFieldDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vRequestFieldDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VRequestField in the database
        List<VRequestField> vRequestFieldList = vRequestFieldRepository.findAll();
        assertThat(vRequestFieldList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVRequestField() throws Exception {
        int databaseSizeBeforeUpdate = vRequestFieldRepository.findAll().size();
        vRequestField.setId(count.incrementAndGet());

        // Create the VRequestField
        VRequestFieldDTO vRequestFieldDTO = vRequestFieldMapper.toDto(vRequestField);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVRequestFieldMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vRequestFieldDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VRequestField in the database
        List<VRequestField> vRequestFieldList = vRequestFieldRepository.findAll();
        assertThat(vRequestFieldList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVRequestField() throws Exception {
        int databaseSizeBeforeUpdate = vRequestFieldRepository.findAll().size();
        vRequestField.setId(count.incrementAndGet());

        // Create the VRequestField
        VRequestFieldDTO vRequestFieldDTO = vRequestFieldMapper.toDto(vRequestField);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVRequestFieldMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vRequestFieldDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the VRequestField in the database
        List<VRequestField> vRequestFieldList = vRequestFieldRepository.findAll();
        assertThat(vRequestFieldList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVRequestField() throws Exception {
        // Initialize the database
        vRequestFieldRepository.saveAndFlush(vRequestField);

        int databaseSizeBeforeDelete = vRequestFieldRepository.findAll().size();

        // Delete the vRequestField
        restVRequestFieldMockMvc
            .perform(delete(ENTITY_API_URL_ID, vRequestField.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<VRequestField> vRequestFieldList = vRequestFieldRepository.findAll();
        assertThat(vRequestFieldList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
