package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.VRequestField;
import com.mycompany.myapp.repository.VRequestFieldRepository;
import com.mycompany.myapp.service.VRequestFieldService;
import com.mycompany.myapp.service.dto.VRequestFieldDTO;
import com.mycompany.myapp.service.mapper.VRequestFieldMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link VRequestField}.
 */
@Service
@Transactional
public class VRequestFieldServiceImpl implements VRequestFieldService {

    private final Logger log = LoggerFactory.getLogger(VRequestFieldServiceImpl.class);

    private final VRequestFieldRepository vRequestFieldRepository;

    private final VRequestFieldMapper vRequestFieldMapper;

    public VRequestFieldServiceImpl(VRequestFieldRepository vRequestFieldRepository, VRequestFieldMapper vRequestFieldMapper) {
        this.vRequestFieldRepository = vRequestFieldRepository;
        this.vRequestFieldMapper = vRequestFieldMapper;
    }

    @Override
    public VRequestFieldDTO save(VRequestFieldDTO vRequestFieldDTO) {
        log.debug("Request to save VRequestField : {}", vRequestFieldDTO);
        VRequestField vRequestField = vRequestFieldMapper.toEntity(vRequestFieldDTO);
        vRequestField = vRequestFieldRepository.save(vRequestField);
        return vRequestFieldMapper.toDto(vRequestField);
    }

    @Override
    public VRequestFieldDTO update(VRequestFieldDTO vRequestFieldDTO) {
        log.debug("Request to update VRequestField : {}", vRequestFieldDTO);
        VRequestField vRequestField = vRequestFieldMapper.toEntity(vRequestFieldDTO);
        vRequestField = vRequestFieldRepository.save(vRequestField);
        return vRequestFieldMapper.toDto(vRequestField);
    }

    @Override
    public Optional<VRequestFieldDTO> partialUpdate(VRequestFieldDTO vRequestFieldDTO) {
        log.debug("Request to partially update VRequestField : {}", vRequestFieldDTO);

        return vRequestFieldRepository
            .findById(vRequestFieldDTO.getId())
            .map(existingVRequestField -> {
                vRequestFieldMapper.partialUpdate(existingVRequestField, vRequestFieldDTO);

                return existingVRequestField;
            })
            .map(vRequestFieldRepository::save)
            .map(vRequestFieldMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VRequestFieldDTO> findAll(Pageable pageable) {
        log.debug("Request to get all VRequestFields");
        return vRequestFieldRepository.findAll(pageable).map(vRequestFieldMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<VRequestFieldDTO> findOne(Long id) {
        log.debug("Request to get VRequestField : {}", id);
        return vRequestFieldRepository.findById(id).map(vRequestFieldMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete VRequestField : {}", id);
        vRequestFieldRepository.deleteById(id);
    }
}
