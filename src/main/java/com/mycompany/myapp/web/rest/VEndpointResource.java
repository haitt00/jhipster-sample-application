package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.VEndpointRepository;
import com.mycompany.myapp.service.VEndpointService;
import com.mycompany.myapp.service.dto.VEndpointDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.VEndpoint}.
 */
@RestController
@RequestMapping("/api")
public class VEndpointResource {

    private final Logger log = LoggerFactory.getLogger(VEndpointResource.class);

    private static final String ENTITY_NAME = "vEndpoint";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VEndpointService vEndpointService;

    private final VEndpointRepository vEndpointRepository;

    public VEndpointResource(VEndpointService vEndpointService, VEndpointRepository vEndpointRepository) {
        this.vEndpointService = vEndpointService;
        this.vEndpointRepository = vEndpointRepository;
    }

    /**
     * {@code POST  /v-endpoints} : Create a new vEndpoint.
     *
     * @param vEndpointDTO the vEndpointDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vEndpointDTO, or with status {@code 400 (Bad Request)} if the vEndpoint has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/v-endpoints")
    public ResponseEntity<VEndpointDTO> createVEndpoint(@RequestBody VEndpointDTO vEndpointDTO) throws URISyntaxException {
        log.debug("REST request to save VEndpoint : {}", vEndpointDTO);
        if (vEndpointDTO.getId() != null) {
            throw new BadRequestAlertException("A new vEndpoint cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VEndpointDTO result = vEndpointService.save(vEndpointDTO);
        return ResponseEntity
            .created(new URI("/api/v-endpoints/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /v-endpoints/:id} : Updates an existing vEndpoint.
     *
     * @param id the id of the vEndpointDTO to save.
     * @param vEndpointDTO the vEndpointDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vEndpointDTO,
     * or with status {@code 400 (Bad Request)} if the vEndpointDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vEndpointDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/v-endpoints/{id}")
    public ResponseEntity<VEndpointDTO> updateVEndpoint(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody VEndpointDTO vEndpointDTO
    ) throws URISyntaxException {
        log.debug("REST request to update VEndpoint : {}, {}", id, vEndpointDTO);
        if (vEndpointDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vEndpointDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vEndpointRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        VEndpointDTO result = vEndpointService.update(vEndpointDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vEndpointDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /v-endpoints/:id} : Partial updates given fields of an existing vEndpoint, field will ignore if it is null
     *
     * @param id the id of the vEndpointDTO to save.
     * @param vEndpointDTO the vEndpointDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vEndpointDTO,
     * or with status {@code 400 (Bad Request)} if the vEndpointDTO is not valid,
     * or with status {@code 404 (Not Found)} if the vEndpointDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the vEndpointDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/v-endpoints/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VEndpointDTO> partialUpdateVEndpoint(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody VEndpointDTO vEndpointDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update VEndpoint partially : {}, {}", id, vEndpointDTO);
        if (vEndpointDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vEndpointDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vEndpointRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VEndpointDTO> result = vEndpointService.partialUpdate(vEndpointDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vEndpointDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /v-endpoints} : get all the vEndpoints.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vEndpoints in body.
     */
    @GetMapping("/v-endpoints")
    public ResponseEntity<List<VEndpointDTO>> getAllVEndpoints(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of VEndpoints");
        Page<VEndpointDTO> page = vEndpointService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /v-endpoints/:id} : get the "id" vEndpoint.
     *
     * @param id the id of the vEndpointDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vEndpointDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/v-endpoints/{id}")
    public ResponseEntity<VEndpointDTO> getVEndpoint(@PathVariable Long id) {
        log.debug("REST request to get VEndpoint : {}", id);
        Optional<VEndpointDTO> vEndpointDTO = vEndpointService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vEndpointDTO);
    }

    /**
     * {@code DELETE  /v-endpoints/:id} : delete the "id" vEndpoint.
     *
     * @param id the id of the vEndpointDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/v-endpoints/{id}")
    public ResponseEntity<Void> deleteVEndpoint(@PathVariable Long id) {
        log.debug("REST request to delete VEndpoint : {}", id);
        vEndpointService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
