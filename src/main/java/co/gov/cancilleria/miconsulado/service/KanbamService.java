package co.gov.cancilleria.miconsulado.service;

import co.gov.cancilleria.miconsulado.domain.main.Kanbam;
import co.gov.cancilleria.miconsulado.service.dto.KanbamDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Kanbam}.
 */
public interface KanbamService {

    /**
     * Save a kanbam.
     *
     * @param kanbamDTO the entity to save.
     * @return the persisted entity.
     */
    KanbamDTO save(KanbamDTO kanbamDTO);

    /**
     * Get all the kanbams.
     *
     * @return the list of entities.
     */
    List<KanbamDTO> findAll();


    /**
     * Get the "id" kanbam.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<KanbamDTO> findOne(Long id);

    /**
     * Delete the "id" kanbam.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
