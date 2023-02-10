package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.VRequestFieldRepository;
import com.mycompany.myapp.service.VRequestFieldService;
import com.mycompany.myapp.service.dto.VRequestFieldDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.VRequestField}.
 */
@RestController
@RequestMapping("/api")
public class VRequestFieldResource {

    private final Logger log = LoggerFactory.getLogger(VRequestFieldResource.class);

    private static final String ENTITY_NAME = "vRequestField";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VRequestFieldService vRequestFieldService;

    private final VRequestFieldRepository vRequestFieldRepository;

    public VRequestFieldResource(VRequestFieldService vRequestFieldService, VRequestFieldRepository vRequestFieldRepository) {
        this.vRequestFieldService = vRequestFieldService;
        this.vRequestFieldRepository = vRequestFieldRepository;
    }

    /**
     * {@code POST  /v-request-fields} : Create a new vRequestField.
     *
     * @param vRequestFieldDTO the vRequestFieldDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vRequestFieldDTO, or with status {@code 400 (Bad Request)} if the vRequestField has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/v-request-fields")
    public ResponseEntity<VRequestFieldDTO> createVRequestField(@RequestBody VRequestFieldDTO vRequestFieldDTO) throws URISyntaxException {
        log.debug("REST request to save VRequestField : {}", vRequestFieldDTO);
        if (vRequestFieldDTO.getId() != null) {
            throw new BadRequestAlertException("A new vRequestField cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VRequestFieldDTO result = vRequestFieldService.save(vRequestFieldDTO);
        return ResponseEntity
            .created(new URI("/api/v-request-fields/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /v-request-fields/:id} : Updates an existing vRequestField.
     *
     * @param id the id of the vRequestFieldDTO to save.
     * @param vRequestFieldDTO the vRequestFieldDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vRequestFieldDTO,
     * or with status {@code 400 (Bad Request)} if the vRequestFieldDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vRequestFieldDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/v-request-fields/{id}")
    public ResponseEntity<VRequestFieldDTO> updateVRequestField(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody VRequestFieldDTO vRequestFieldDTO
    ) throws URISyntaxException {
        log.debug("REST request to update VRequestField : {}, {}", id, vRequestFieldDTO);
        if (vRequestFieldDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vRequestFieldDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vRequestFieldRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        VRequestFieldDTO result = vRequestFieldService.update(vRequestFieldDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vRequestFieldDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /v-request-fields/:id} : Partial updates given fields of an existing vRequestField, field will ignore if it is null
     *
     * @param id the id of the vRequestFieldDTO to save.
     * @param vRequestFieldDTO the vRequestFieldDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vRequestFieldDTO,
     * or with status {@code 400 (Bad Request)} if the vRequestFieldDTO is not valid,
     * or with status {@code 404 (Not Found)} if the vRequestFieldDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the vRequestFieldDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/v-request-fields/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VRequestFieldDTO> partialUpdateVRequestField(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody VRequestFieldDTO vRequestFieldDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update VRequestField partially : {}, {}", id, vRequestFieldDTO);
        if (vRequestFieldDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vRequestFieldDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vRequestFieldRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VRequestFieldDTO> result = vRequestFieldService.partialUpdate(vRequestFieldDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vRequestFieldDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /v-request-fields} : get all the vRequestFields.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vRequestFields in body.
     */
    @GetMapping("/v-request-fields")
    public ResponseEntity<List<VRequestFieldDTO>> getAllVRequestFields(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of VRequestFields");
        Page<VRequestFieldDTO> page = vRequestFieldService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /v-request-fields/:id} : get the "id" vRequestField.
     *
     * @param id the id of the vRequestFieldDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vRequestFieldDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/v-request-fields/{id}")
    public ResponseEntity<VRequestFieldDTO> getVRequestField(@PathVariable Long id) {
        log.debug("REST request to get VRequestField : {}", id);
        Optional<VRequestFieldDTO> vRequestFieldDTO = vRequestFieldService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vRequestFieldDTO);
    }

    /**
     * {@code DELETE  /v-request-fields/:id} : delete the "id" vRequestField.
     *
     * @param id the id of the vRequestFieldDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/v-request-fields/{id}")
    public ResponseEntity<Void> deleteVRequestField(@PathVariable Long id) {
        log.debug("REST request to delete VRequestField : {}", id);
        vRequestFieldService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
