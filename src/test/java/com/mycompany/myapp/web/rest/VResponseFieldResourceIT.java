package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.VResponseField;
import com.mycompany.myapp.repository.VResponseFieldRepository;
import com.mycompany.myapp.service.dto.VResponseFieldDTO;
import com.mycompany.myapp.service.mapper.VResponseFieldMapper;
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
 * Integration tests for the {@link VResponseFieldResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VResponseFieldResourceIT {

    private static final Long DEFAULT_ENDPOINT_ID = 1L;
    private static final Long UPDATED_ENDPOINT_ID = 2L;

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/v-response-fields";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VResponseFieldRepository vResponseFieldRepository;

    @Autowired
    private VResponseFieldMapper vResponseFieldMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVResponseFieldMockMvc;

    private VResponseField vResponseField;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VResponseField createEntity(EntityManager em) {
        VResponseField vResponseField = new VResponseField().endpointId(DEFAULT_ENDPOINT_ID).code(DEFAULT_CODE).name(DEFAULT_NAME);
        return vResponseField;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VResponseField createUpdatedEntity(EntityManager em) {
        VResponseField vResponseField = new VResponseField().endpointId(UPDATED_ENDPOINT_ID).code(UPDATED_CODE).name(UPDATED_NAME);
        return vResponseField;
    }

    @BeforeEach
    public void initTest() {
        vResponseField = createEntity(em);
    }

    @Test
    @Transactional
    void createVResponseField() throws Exception {
        int databaseSizeBeforeCreate = vResponseFieldRepository.findAll().size();
        // Create the VResponseField
        VResponseFieldDTO vResponseFieldDTO = vResponseFieldMapper.toDto(vResponseField);
        restVResponseFieldMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vResponseFieldDTO))
            )
            .andExpect(status().isCreated());

        // Validate the VResponseField in the database
        List<VResponseField> vResponseFieldList = vResponseFieldRepository.findAll();
        assertThat(vResponseFieldList).hasSize(databaseSizeBeforeCreate + 1);
        VResponseField testVResponseField = vResponseFieldList.get(vResponseFieldList.size() - 1);
        assertThat(testVResponseField.getEndpointId()).isEqualTo(DEFAULT_ENDPOINT_ID);
        assertThat(testVResponseField.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testVResponseField.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createVResponseFieldWithExistingId() throws Exception {
        // Create the VResponseField with an existing ID
        vResponseField.setId(1L);
        VResponseFieldDTO vResponseFieldDTO = vResponseFieldMapper.toDto(vResponseField);

        int databaseSizeBeforeCreate = vResponseFieldRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVResponseFieldMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vResponseFieldDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VResponseField in the database
        List<VResponseField> vResponseFieldList = vResponseFieldRepository.findAll();
        assertThat(vResponseFieldList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllVResponseFields() throws Exception {
        // Initialize the database
        vResponseFieldRepository.saveAndFlush(vResponseField);

        // Get all the vResponseFieldList
        restVResponseFieldMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vResponseField.getId().intValue())))
            .andExpect(jsonPath("$.[*].endpointId").value(hasItem(DEFAULT_ENDPOINT_ID.intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getVResponseField() throws Exception {
        // Initialize the database
        vResponseFieldRepository.saveAndFlush(vResponseField);

        // Get the vResponseField
        restVResponseFieldMockMvc
            .perform(get(ENTITY_API_URL_ID, vResponseField.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vResponseField.getId().intValue()))
            .andExpect(jsonPath("$.endpointId").value(DEFAULT_ENDPOINT_ID.intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingVResponseField() throws Exception {
        // Get the vResponseField
        restVResponseFieldMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVResponseField() throws Exception {
        // Initialize the database
        vResponseFieldRepository.saveAndFlush(vResponseField);

        int databaseSizeBeforeUpdate = vResponseFieldRepository.findAll().size();

        // Update the vResponseField
        VResponseField updatedVResponseField = vResponseFieldRepository.findById(vResponseField.getId()).get();
        // Disconnect from session so that the updates on updatedVResponseField are not directly saved in db
        em.detach(updatedVResponseField);
        updatedVResponseField.endpointId(UPDATED_ENDPOINT_ID).code(UPDATED_CODE).name(UPDATED_NAME);
        VResponseFieldDTO vResponseFieldDTO = vResponseFieldMapper.toDto(updatedVResponseField);

        restVResponseFieldMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vResponseFieldDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vResponseFieldDTO))
            )
            .andExpect(status().isOk());

        // Validate the VResponseField in the database
        List<VResponseField> vResponseFieldList = vResponseFieldRepository.findAll();
        assertThat(vResponseFieldList).hasSize(databaseSizeBeforeUpdate);
        VResponseField testVResponseField = vResponseFieldList.get(vResponseFieldList.size() - 1);
        assertThat(testVResponseField.getEndpointId()).isEqualTo(UPDATED_ENDPOINT_ID);
        assertThat(testVResponseField.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testVResponseField.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingVResponseField() throws Exception {
        int databaseSizeBeforeUpdate = vResponseFieldRepository.findAll().size();
        vResponseField.setId(count.incrementAndGet());

        // Create the VResponseField
        VResponseFieldDTO vResponseFieldDTO = vResponseFieldMapper.toDto(vResponseField);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVResponseFieldMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vResponseFieldDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vResponseFieldDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VResponseField in the database
        List<VResponseField> vResponseFieldList = vResponseFieldRepository.findAll();
        assertThat(vResponseFieldList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVResponseField() throws Exception {
        int databaseSizeBeforeUpdate = vResponseFieldRepository.findAll().size();
        vResponseField.setId(count.incrementAndGet());

        // Create the VResponseField
        VResponseFieldDTO vResponseFieldDTO = vResponseFieldMapper.toDto(vResponseField);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVResponseFieldMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vResponseFieldDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VResponseField in the database
        List<VResponseField> vResponseFieldList = vResponseFieldRepository.findAll();
        assertThat(vResponseFieldList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVResponseField() throws Exception {
        int databaseSizeBeforeUpdate = vResponseFieldRepository.findAll().size();
        vResponseField.setId(count.incrementAndGet());

        // Create the VResponseField
        VResponseFieldDTO vResponseFieldDTO = vResponseFieldMapper.toDto(vResponseField);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVResponseFieldMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vResponseFieldDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the VResponseField in the database
        List<VResponseField> vResponseFieldList = vResponseFieldRepository.findAll();
        assertThat(vResponseFieldList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVResponseFieldWithPatch() throws Exception {
        // Initialize the database
        vResponseFieldRepository.saveAndFlush(vResponseField);

        int databaseSizeBeforeUpdate = vResponseFieldRepository.findAll().size();

        // Update the vResponseField using partial update
        VResponseField partialUpdatedVResponseField = new VResponseField();
        partialUpdatedVResponseField.setId(vResponseField.getId());

        partialUpdatedVResponseField.endpointId(UPDATED_ENDPOINT_ID).code(UPDATED_CODE).name(UPDATED_NAME);

        restVResponseFieldMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVResponseField.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVResponseField))
            )
            .andExpect(status().isOk());

        // Validate the VResponseField in the database
        List<VResponseField> vResponseFieldList = vResponseFieldRepository.findAll();
        assertThat(vResponseFieldList).hasSize(databaseSizeBeforeUpdate);
        VResponseField testVResponseField = vResponseFieldList.get(vResponseFieldList.size() - 1);
        assertThat(testVResponseField.getEndpointId()).isEqualTo(UPDATED_ENDPOINT_ID);
        assertThat(testVResponseField.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testVResponseField.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateVResponseFieldWithPatch() throws Exception {
        // Initialize the database
        vResponseFieldRepository.saveAndFlush(vResponseField);

        int databaseSizeBeforeUpdate = vResponseFieldRepository.findAll().size();

        // Update the vResponseField using partial update
        VResponseField partialUpdatedVResponseField = new VResponseField();
        partialUpdatedVResponseField.setId(vResponseField.getId());

        partialUpdatedVResponseField.endpointId(UPDATED_ENDPOINT_ID).code(UPDATED_CODE).name(UPDATED_NAME);

        restVResponseFieldMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVResponseField.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVResponseField))
            )
            .andExpect(status().isOk());

        // Validate the VResponseField in the database
        List<VResponseField> vResponseFieldList = vResponseFieldRepository.findAll();
        assertThat(vResponseFieldList).hasSize(databaseSizeBeforeUpdate);
        VResponseField testVResponseField = vResponseFieldList.get(vResponseFieldList.size() - 1);
        assertThat(testVResponseField.getEndpointId()).isEqualTo(UPDATED_ENDPOINT_ID);
        assertThat(testVResponseField.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testVResponseField.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingVResponseField() throws Exception {
        int databaseSizeBeforeUpdate = vResponseFieldRepository.findAll().size();
        vResponseField.setId(count.incrementAndGet());

        // Create the VResponseField
        VResponseFieldDTO vResponseFieldDTO = vResponseFieldMapper.toDto(vResponseField);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVResponseFieldMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vResponseFieldDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vResponseFieldDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VResponseField in the database
        List<VResponseField> vResponseFieldList = vResponseFieldRepository.findAll();
        assertThat(vResponseFieldList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVResponseField() throws Exception {
        int databaseSizeBeforeUpdate = vResponseFieldRepository.findAll().size();
        vResponseField.setId(count.incrementAndGet());

        // Create the VResponseField
        VResponseFieldDTO vResponseFieldDTO = vResponseFieldMapper.toDto(vResponseField);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVResponseFieldMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vResponseFieldDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VResponseField in the database
        List<VResponseField> vResponseFieldList = vResponseFieldRepository.findAll();
        assertThat(vResponseFieldList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVResponseField() throws Exception {
        int databaseSizeBeforeUpdate = vResponseFieldRepository.findAll().size();
        vResponseField.setId(count.incrementAndGet());

        // Create the VResponseField
        VResponseFieldDTO vResponseFieldDTO = vResponseFieldMapper.toDto(vResponseField);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVResponseFieldMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vResponseFieldDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the VResponseField in the database
        List<VResponseField> vResponseFieldList = vResponseFieldRepository.findAll();
        assertThat(vResponseFieldList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVResponseField() throws Exception {
        // Initialize the database
        vResponseFieldRepository.saveAndFlush(vResponseField);

        int databaseSizeBeforeDelete = vResponseFieldRepository.findAll().size();

        // Delete the vResponseField
        restVResponseFieldMockMvc
            .perform(delete(ENTITY_API_URL_ID, vResponseField.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<VResponseField> vResponseFieldList = vResponseFieldRepository.findAll();
        assertThat(vResponseFieldList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
