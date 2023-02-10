package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.VEndpoint;
import com.mycompany.myapp.repository.VEndpointRepository;
import com.mycompany.myapp.service.VEndpointService;
import com.mycompany.myapp.service.dto.VEndpointDTO;
import com.mycompany.myapp.service.mapper.VEndpointMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link VEndpoint}.
 */
@Service
@Transactional
public class VEndpointServiceImpl implements VEndpointService {

    private final Logger log = LoggerFactory.getLogger(VEndpointServiceImpl.class);

    private final VEndpointRepository vEndpointRepository;

    private final VEndpointMapper vEndpointMapper;

    public VEndpointServiceImpl(VEndpointRepository vEndpointRepository, VEndpointMapper vEndpointMapper) {
        this.vEndpointRepository = vEndpointRepository;
        this.vEndpointMapper = vEndpointMapper;
    }

    @Override
    public VEndpointDTO save(VEndpointDTO vEndpointDTO) {
        log.debug("Request to save VEndpoint : {}", vEndpointDTO);
        VEndpoint vEndpoint = vEndpointMapper.toEntity(vEndpointDTO);
        vEndpoint = vEndpointRepository.save(vEndpoint);
        return vEndpointMapper.toDto(vEndpoint);
    }

    @Override
    public VEndpointDTO update(VEndpointDTO vEndpointDTO) {
        log.debug("Request to update VEndpoint : {}", vEndpointDTO);
        VEndpoint vEndpoint = vEndpointMapper.toEntity(vEndpointDTO);
        vEndpoint = vEndpointRepository.save(vEndpoint);
        return vEndpointMapper.toDto(vEndpoint);
    }

    @Override
    public Optional<VEndpointDTO> partialUpdate(VEndpointDTO vEndpointDTO) {
        log.debug("Request to partially update VEndpoint : {}", vEndpointDTO);

        return vEndpointRepository
            .findById(vEndpointDTO.getId())
            .map(existingVEndpoint -> {
                vEndpointMapper.partialUpdate(existingVEndpoint, vEndpointDTO);

                return existingVEndpoint;
            })
            .map(vEndpointRepository::save)
            .map(vEndpointMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VEndpointDTO> findAll(Pageable pageable) {
        log.debug("Request to get all VEndpoints");
        return vEndpointRepository.findAll(pageable).map(vEndpointMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<VEndpointDTO> findOne(Long id) {
        log.debug("Request to get VEndpoint : {}", id);
        return vEndpointRepository.findById(id).map(vEndpointMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete VEndpoint : {}", id);
        vEndpointRepository.deleteById(id);
    }
}
