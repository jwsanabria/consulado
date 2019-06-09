package co.gov.cancilleria.miconsulado.service.impl;

import co.gov.cancilleria.miconsulado.service.KanbamService;
import co.gov.cancilleria.miconsulado.domain.Kanbam;
import co.gov.cancilleria.miconsulado.repository.KanbamRepository;
import co.gov.cancilleria.miconsulado.service.dto.KanbamDTO;
import co.gov.cancilleria.miconsulado.service.mapper.KanbamMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Kanbam}.
 */
@Service
@Transactional
public class KanbamServiceImpl implements KanbamService {

    private final Logger log = LoggerFactory.getLogger(KanbamServiceImpl.class);

    private final KanbamRepository kanbamRepository;

    private final KanbamMapper kanbamMapper;

    public KanbamServiceImpl(KanbamRepository kanbamRepository, KanbamMapper kanbamMapper) {
        this.kanbamRepository = kanbamRepository;
        this.kanbamMapper = kanbamMapper;
    }

    /**
     * Save a kanbam.
     *
     * @param kanbamDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public KanbamDTO save(KanbamDTO kanbamDTO) {
        log.debug("Request to save Kanbam : {}", kanbamDTO);
        Kanbam kanbam = kanbamMapper.toEntity(kanbamDTO);
        kanbam = kanbamRepository.save(kanbam);
        return kanbamMapper.toDto(kanbam);
    }

    /**
     * Get all the kanbams.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<KanbamDTO> findAll() {
        log.debug("Request to get all Kanbams");
        return kanbamRepository.findAll().stream()
            .map(kanbamMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one kanbam by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<KanbamDTO> findOne(Long id) {
        log.debug("Request to get Kanbam : {}", id);
        return kanbamRepository.findById(id)
            .map(kanbamMapper::toDto);
    }

    /**
     * Delete the kanbam by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Kanbam : {}", id);
        kanbamRepository.deleteById(id);
    }
}
