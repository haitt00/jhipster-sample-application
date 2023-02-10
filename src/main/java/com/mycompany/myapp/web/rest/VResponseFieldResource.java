package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.VResponseFieldRepository;
import com.mycompany.myapp.service.VResponseFieldService;
import com.mycompany.myapp.service.dto.VResponseFieldDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.VResponseField}.
 */
@RestController
@RequestMapping("/api")
public class VResponseFieldResource {

    private final Logger log = LoggerFactory.getLogger(VResponseFieldResource.class);

    private static final String ENTITY_NAME = "vResponseField";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VResponseFieldService vResponseFieldService;

    private final VResponseFieldRepository vResponseFieldRepository;

    public VResponseFieldResource(VResponseFieldService vResponseFieldService, VResponseFieldRepository vResponseFieldRepository) {
        this.vResponseFieldService = vResponseFieldService;
        this.vResponseFieldRepository = vResponseFieldRepository;
    }

    /**
     * {@code POST  /v-response-fields} : Create a new vResponseField.
     *
     * @param vResponseFieldDTO the vResponseFieldDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vResponseFieldDTO, or with status {@code 400 (Bad Request)} if the vResponseField has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/v-response-fields")
    public ResponseEntity<VResponseFieldDTO> createVResponseField(@RequestBody VResponseFieldDTO vResponseFieldDTO)
        throws URISyntaxException {
        log.debug("REST request to save VResponseField : {}", vResponseFieldDTO);
        if (vResponseFieldDTO.getId() != null) {
            throw new BadRequestAlertException("A new vResponseField cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VResponseFieldDTO result = vResponseFieldService.save(vResponseFieldDTO);
        return ResponseEntity
            .created(new URI("/api/v-response-fields/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /v-response-fields/:id} : Updates an existing vResponseField.
     *
     * @param id the id of the vResponseFieldDTO to save.
     * @param vResponseFieldDTO the vResponseFieldDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vResponseFieldDTO,
     * or with status {@code 400 (Bad Request)} if the vResponseFieldDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vResponseFieldDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/v-response-fields/{id}")
    public ResponseEntity<VResponseFieldDTO> updateVResponseField(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody VResponseFieldDTO vResponseFieldDTO
    ) throws URISyntaxException {
        log.debug("REST request to update VResponseField : {}, {}", id, vResponseFieldDTO);
        if (vResponseFieldDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vResponseFieldDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vResponseFieldRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        VResponseFieldDTO result = vResponseFieldService.update(vResponseFieldDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vResponseFieldDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /v-response-fields/:id} : Partial updates given fields of an existing vResponseField, field will ignore if it is null
     *
     * @param id the id of the vResponseFieldDTO to save.
     * @param vResponseFieldDTO the vResponseFieldDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vResponseFieldDTO,
     * or with status {@code 400 (Bad Request)} if the vResponseFieldDTO is not valid,
     * or with status {@code 404 (Not Found)} if the vResponseFieldDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the vResponseFieldDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/v-response-fields/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VResponseFieldDTO> partialUpdateVResponseField(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody VResponseFieldDTO vResponseFieldDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update VResponseField partially : {}, {}", id, vResponseFieldDTO);
        if (vResponseFieldDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vResponseFieldDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vResponseFieldRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VResponseFieldDTO> result = vResponseFieldService.partialUpdate(vResponseFieldDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vResponseFieldDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /v-response-fields} : get all the vResponseFields.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vResponseFields in body.
     */
    @GetMapping("/v-response-fields")
    public ResponseEntity<List<VResponseFieldDTO>> getAllVResponseFields(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of VResponseFields");
        Page<VResponseFieldDTO> page = vResponseFieldService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /v-response-fields/:id} : get the "id" vResponseField.
     *
     * @param id the id of the vResponseFieldDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vResponseFieldDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/v-response-fields/{id}")
    public ResponseEntity<VResponseFieldDTO> getVResponseField(@PathVariable Long id) {
        log.debug("REST request to get VResponseField : {}", id);
        Optional<VResponseFieldDTO> vResponseFieldDTO = vResponseFieldService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vResponseFieldDTO);
    }

    /**
     * {@code DELETE  /v-response-fields/:id} : delete the "id" vResponseField.
     *
     * @param id the id of the vResponseFieldDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/v-response-fields/{id}")
    public ResponseEntity<Void> deleteVResponseField(@PathVariable Long id) {
        log.debug("REST request to delete VResponseField : {}", id);
        vResponseFieldService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
