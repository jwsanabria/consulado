package co.gov.cancilleria.miconsulado.service.impl;

import co.gov.cancilleria.miconsulado.domain.mds.Office;
import co.gov.cancilleria.miconsulado.repository.mds.OfficeRepository;
import co.gov.cancilleria.miconsulado.service.MdsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service Implementation for managing {@link Of}.
 */
@Service
@Transactional
public class MdsServiceImpl implements MdsService {

    private final Logger log = LoggerFactory.getLogger(MdsServiceImpl.class);

    private final OfficeRepository officeRepository;

    public MdsServiceImpl(OfficeRepository officeRepository) {
        this.officeRepository = officeRepository;
    }


    /**
     * Get all the tasks.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Office> findAvaliableOffices() {
        log.debug("Request to get avaliable offices");
        return officeRepository.findAvaliable();
    }

}
