package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.VResponseField;
import com.mycompany.myapp.repository.VResponseFieldRepository;
import com.mycompany.myapp.service.VResponseFieldService;
import com.mycompany.myapp.service.dto.VResponseFieldDTO;
import com.mycompany.myapp.service.mapper.VResponseFieldMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link VResponseField}.
 */
@Service
@Transactional
public class VResponseFieldServiceImpl implements VResponseFieldService {

    private final Logger log = LoggerFactory.getLogger(VResponseFieldServiceImpl.class);

    private final VResponseFieldRepository vResponseFieldRepository;

    private final VResponseFieldMapper vResponseFieldMapper;

    public VResponseFieldServiceImpl(VResponseFieldRepository vResponseFieldRepository, VResponseFieldMapper vResponseFieldMapper) {
        this.vResponseFieldRepository = vResponseFieldRepository;
        this.vResponseFieldMapper = vResponseFieldMapper;
    }

    @Override
    public VResponseFieldDTO save(VResponseFieldDTO vResponseFieldDTO) {
        log.debug("Request to save VResponseField : {}", vResponseFieldDTO);
        VResponseField vResponseField = vResponseFieldMapper.toEntity(vResponseFieldDTO);
        vResponseField = vResponseFieldRepository.save(vResponseField);
        return vResponseFieldMapper.toDto(vResponseField);
    }

    @Override
    public VResponseFieldDTO update(VResponseFieldDTO vResponseFieldDTO) {
        log.debug("Request to update VResponseField : {}", vResponseFieldDTO);
        VResponseField vResponseField = vResponseFieldMapper.toEntity(vResponseFieldDTO);
        vResponseField = vResponseFieldRepository.save(vResponseField);
        return vResponseFieldMapper.toDto(vResponseField);
    }

    @Override
    public Optional<VResponseFieldDTO> partialUpdate(VResponseFieldDTO vResponseFieldDTO) {
        log.debug("Request to partially update VResponseField : {}", vResponseFieldDTO);

        return vResponseFieldRepository
            .findById(vResponseFieldDTO.getId())
            .map(existingVResponseField -> {
                vResponseFieldMapper.partialUpdate(existingVResponseField, vResponseFieldDTO);

                return existingVResponseField;
            })
            .map(vResponseFieldRepository::save)
            .map(vResponseFieldMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VResponseFieldDTO> findAll(Pageable pageable) {
        log.debug("Request to get all VResponseFields");
        return vResponseFieldRepository.findAll(pageable).map(vResponseFieldMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<VResponseFieldDTO> findOne(Long id) {
        log.debug("Request to get VResponseField : {}", id);
        return vResponseFieldRepository.findById(id).map(vResponseFieldMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete VResponseField : {}", id);
        vResponseFieldRepository.deleteById(id);
    }
}
