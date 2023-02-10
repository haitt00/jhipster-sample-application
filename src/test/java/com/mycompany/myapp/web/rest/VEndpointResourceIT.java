package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.VEndpoint;
import com.mycompany.myapp.repository.VEndpointRepository;
import com.mycompany.myapp.service.dto.VEndpointDTO;
import com.mycompany.myapp.service.mapper.VEndpointMapper;
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
 * Integration tests for the {@link VEndpointResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VEndpointResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String DEFAULT_METHOD = "AAAAAAAAAA";
    private static final String UPDATED_METHOD = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/v-endpoints";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VEndpointRepository vEndpointRepository;

    @Autowired
    private VEndpointMapper vEndpointMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVEndpointMockMvc;

    private VEndpoint vEndpoint;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VEndpoint createEntity(EntityManager em) {
        VEndpoint vEndpoint = new VEndpoint()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .url(DEFAULT_URL)
            .method(DEFAULT_METHOD);
        return vEndpoint;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VEndpoint createUpdatedEntity(EntityManager em) {
        VEndpoint vEndpoint = new VEndpoint()
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .url(UPDATED_URL)
            .method(UPDATED_METHOD);
        return vEndpoint;
    }

    @BeforeEach
    public void initTest() {
        vEndpoint = createEntity(em);
    }

    @Test
    @Transactional
    void createVEndpoint() throws Exception {
        int databaseSizeBeforeCreate = vEndpointRepository.findAll().size();
        // Create the VEndpoint
        VEndpointDTO vEndpointDTO = vEndpointMapper.toDto(vEndpoint);
        restVEndpointMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vEndpointDTO)))
            .andExpect(status().isCreated());

        // Validate the VEndpoint in the database
        List<VEndpoint> vEndpointList = vEndpointRepository.findAll();
        assertThat(vEndpointList).hasSize(databaseSizeBeforeCreate + 1);
        VEndpoint testVEndpoint = vEndpointList.get(vEndpointList.size() - 1);
        assertThat(testVEndpoint.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testVEndpoint.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testVEndpoint.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testVEndpoint.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testVEndpoint.getMethod()).isEqualTo(DEFAULT_METHOD);
    }

    @Test
    @Transactional
    void createVEndpointWithExistingId() throws Exception {
        // Create the VEndpoint with an existing ID
        vEndpoint.setId(1L);
        VEndpointDTO vEndpointDTO = vEndpointMapper.toDto(vEndpoint);

        int databaseSizeBeforeCreate = vEndpointRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVEndpointMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vEndpointDTO)))
            .andExpect(status().isBadRequest());

        // Validate the VEndpoint in the database
        List<VEndpoint> vEndpointList = vEndpointRepository.findAll();
        assertThat(vEndpointList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllVEndpoints() throws Exception {
        // Initialize the database
        vEndpointRepository.saveAndFlush(vEndpoint);

        // Get all the vEndpointList
        restVEndpointMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vEndpoint.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].method").value(hasItem(DEFAULT_METHOD)));
    }

    @Test
    @Transactional
    void getVEndpoint() throws Exception {
        // Initialize the database
        vEndpointRepository.saveAndFlush(vEndpoint);

        // Get the vEndpoint
        restVEndpointMockMvc
            .perform(get(ENTITY_API_URL_ID, vEndpoint.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vEndpoint.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL))
            .andExpect(jsonPath("$.method").value(DEFAULT_METHOD));
    }

    @Test
    @Transactional
    void getNonExistingVEndpoint() throws Exception {
        // Get the vEndpoint
        restVEndpointMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVEndpoint() throws Exception {
        // Initialize the database
        vEndpointRepository.saveAndFlush(vEndpoint);

        int databaseSizeBeforeUpdate = vEndpointRepository.findAll().size();

        // Update the vEndpoint
        VEndpoint updatedVEndpoint = vEndpointRepository.findById(vEndpoint.getId()).get();
        // Disconnect from session so that the updates on updatedVEndpoint are not directly saved in db
        em.detach(updatedVEndpoint);
        updatedVEndpoint.code(UPDATED_CODE).name(UPDATED_NAME).description(UPDATED_DESCRIPTION).url(UPDATED_URL).method(UPDATED_METHOD);
        VEndpointDTO vEndpointDTO = vEndpointMapper.toDto(updatedVEndpoint);

        restVEndpointMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vEndpointDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vEndpointDTO))
            )
            .andExpect(status().isOk());

        // Validate the VEndpoint in the database
        List<VEndpoint> vEndpointList = vEndpointRepository.findAll();
        assertThat(vEndpointList).hasSize(databaseSizeBeforeUpdate);
        VEndpoint testVEndpoint = vEndpointList.get(vEndpointList.size() - 1);
        assertThat(testVEndpoint.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testVEndpoint.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testVEndpoint.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testVEndpoint.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testVEndpoint.getMethod()).isEqualTo(UPDATED_METHOD);
    }

    @Test
    @Transactional
    void putNonExistingVEndpoint() throws Exception {
        int databaseSizeBeforeUpdate = vEndpointRepository.findAll().size();
        vEndpoint.setId(count.incrementAndGet());

        // Create the VEndpoint
        VEndpointDTO vEndpointDTO = vEndpointMapper.toDto(vEndpoint);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVEndpointMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vEndpointDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vEndpointDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VEndpoint in the database
        List<VEndpoint> vEndpointList = vEndpointRepository.findAll();
        assertThat(vEndpointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVEndpoint() throws Exception {
        int databaseSizeBeforeUpdate = vEndpointRepository.findAll().size();
        vEndpoint.setId(count.incrementAndGet());

        // Create the VEndpoint
        VEndpointDTO vEndpointDTO = vEndpointMapper.toDto(vEndpoint);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVEndpointMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vEndpointDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VEndpoint in the database
        List<VEndpoint> vEndpointList = vEndpointRepository.findAll();
        assertThat(vEndpointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVEndpoint() throws Exception {
        int databaseSizeBeforeUpdate = vEndpointRepository.findAll().size();
        vEndpoint.setId(count.incrementAndGet());

        // Create the VEndpoint
        VEndpointDTO vEndpointDTO = vEndpointMapper.toDto(vEndpoint);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVEndpointMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vEndpointDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the VEndpoint in the database
        List<VEndpoint> vEndpointList = vEndpointRepository.findAll();
        assertThat(vEndpointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVEndpointWithPatch() throws Exception {
        // Initialize the database
        vEndpointRepository.saveAndFlush(vEndpoint);

        int databaseSizeBeforeUpdate = vEndpointRepository.findAll().size();

        // Update the vEndpoint using partial update
        VEndpoint partialUpdatedVEndpoint = new VEndpoint();
        partialUpdatedVEndpoint.setId(vEndpoint.getId());

        partialUpdatedVEndpoint.url(UPDATED_URL);

        restVEndpointMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVEndpoint.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVEndpoint))
            )
            .andExpect(status().isOk());

        // Validate the VEndpoint in the database
        List<VEndpoint> vEndpointList = vEndpointRepository.findAll();
        assertThat(vEndpointList).hasSize(databaseSizeBeforeUpdate);
        VEndpoint testVEndpoint = vEndpointList.get(vEndpointList.size() - 1);
        assertThat(testVEndpoint.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testVEndpoint.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testVEndpoint.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testVEndpoint.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testVEndpoint.getMethod()).isEqualTo(DEFAULT_METHOD);
    }

    @Test
    @Transactional
    void fullUpdateVEndpointWithPatch() throws Exception {
        // Initialize the database
        vEndpointRepository.saveAndFlush(vEndpoint);

        int databaseSizeBeforeUpdate = vEndpointRepository.findAll().size();

        // Update the vEndpoint using partial update
        VEndpoint partialUpdatedVEndpoint = new VEndpoint();
        partialUpdatedVEndpoint.setId(vEndpoint.getId());

        partialUpdatedVEndpoint
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .url(UPDATED_URL)
            .method(UPDATED_METHOD);

        restVEndpointMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVEndpoint.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVEndpoint))
            )
            .andExpect(status().isOk());

        // Validate the VEndpoint in the database
        List<VEndpoint> vEndpointList = vEndpointRepository.findAll();
        assertThat(vEndpointList).hasSize(databaseSizeBeforeUpdate);
        VEndpoint testVEndpoint = vEndpointList.get(vEndpointList.size() - 1);
        assertThat(testVEndpoint.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testVEndpoint.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testVEndpoint.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testVEndpoint.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testVEndpoint.getMethod()).isEqualTo(UPDATED_METHOD);
    }

    @Test
    @Transactional
    void patchNonExistingVEndpoint() throws Exception {
        int databaseSizeBeforeUpdate = vEndpointRepository.findAll().size();
        vEndpoint.setId(count.incrementAndGet());

        // Create the VEndpoint
        VEndpointDTO vEndpointDTO = vEndpointMapper.toDto(vEndpoint);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVEndpointMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vEndpointDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vEndpointDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VEndpoint in the database
        List<VEndpoint> vEndpointList = vEndpointRepository.findAll();
        assertThat(vEndpointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVEndpoint() throws Exception {
        int databaseSizeBeforeUpdate = vEndpointRepository.findAll().size();
        vEndpoint.setId(count.incrementAndGet());

        // Create the VEndpoint
        VEndpointDTO vEndpointDTO = vEndpointMapper.toDto(vEndpoint);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVEndpointMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vEndpointDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VEndpoint in the database
        List<VEndpoint> vEndpointList = vEndpointRepository.findAll();
        assertThat(vEndpointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVEndpoint() throws Exception {
        int databaseSizeBeforeUpdate = vEndpointRepository.findAll().size();
        vEndpoint.setId(count.incrementAndGet());

        // Create the VEndpoint
        VEndpointDTO vEndpointDTO = vEndpointMapper.toDto(vEndpoint);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVEndpointMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(vEndpointDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the VEndpoint in the database
        List<VEndpoint> vEndpointList = vEndpointRepository.findAll();
        assertThat(vEndpointList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVEndpoint() throws Exception {
        // Initialize the database
        vEndpointRepository.saveAndFlush(vEndpoint);

        int databaseSizeBeforeDelete = vEndpointRepository.findAll().size();

        // Delete the vEndpoint
        restVEndpointMockMvc
            .perform(delete(ENTITY_API_URL_ID, vEndpoint.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<VEndpoint> vEndpointList = vEndpointRepository.findAll();
        assertThat(vEndpointList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
