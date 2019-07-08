package co.gov.cancilleria.miconsulado.service;

import co.gov.cancilleria.miconsulado.domain.mds.Office;

import java.util.List;

/**
 * Service Interface for managing {@link Office}.
 */
public interface MdsService {


    /**
     * Get all the avaliable offices.
     *
     * @return the list of entities.
     */
    List<Office> findAvaliableOffices();
}
