package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.CreditCard;
import com.mycompany.myapp.repository.CreditCardRepository;
import com.mycompany.myapp.service.CreditCardService;
import com.mycompany.myapp.service.dto.CreditCardDTO;
import com.mycompany.myapp.service.mapper.CreditCardMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CreditCard}.
 */
@Service
@Transactional
public class CreditCardServiceImpl implements CreditCardService {

    private final Logger log = LoggerFactory.getLogger(CreditCardServiceImpl.class);

    private final CreditCardRepository creditCardRepository;

    private final CreditCardMapper creditCardMapper;

    public CreditCardServiceImpl(CreditCardRepository creditCardRepository, CreditCardMapper creditCardMapper) {
        this.creditCardRepository = creditCardRepository;
        this.creditCardMapper = creditCardMapper;
    }

    @Override
    public CreditCardDTO save(CreditCardDTO creditCardDTO) {
        log.debug("Request to save CreditCard : {}", creditCardDTO);
        CreditCard creditCard = creditCardMapper.toEntity(creditCardDTO);
        creditCard = creditCardRepository.save(creditCard);
        return creditCardMapper.toDto(creditCard);
    }

    @Override
    public CreditCardDTO update(CreditCardDTO creditCardDTO) {
        log.debug("Request to update CreditCard : {}", creditCardDTO);
        CreditCard creditCard = creditCardMapper.toEntity(creditCardDTO);
        creditCard = creditCardRepository.save(creditCard);
        return creditCardMapper.toDto(creditCard);
    }

    @Override
    public Optional<CreditCardDTO> partialUpdate(CreditCardDTO creditCardDTO) {
        log.debug("Request to partially update CreditCard : {}", creditCardDTO);

        return creditCardRepository
            .findById(creditCardDTO.getId())
            .map(existingCreditCard -> {
                creditCardMapper.partialUpdate(existingCreditCard, creditCardDTO);

                return existingCreditCard;
            })
            .map(creditCardRepository::save)
            .map(creditCardMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CreditCardDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CreditCards");
        return creditCardRepository.findAll(pageable).map(creditCardMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CreditCardDTO> findOne(Long id) {
        log.debug("Request to get CreditCard : {}", id);
        return creditCardRepository.findById(id).map(creditCardMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CreditCard : {}", id);
        creditCardRepository.deleteById(id);
    }
}
