package co.gov.cancilleria.miconsulado.web.rest;

import co.gov.cancilleria.miconsulado.domain.mds.Office;
import co.gov.cancilleria.miconsulado.service.MdsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller to MDS.
 */
@RestController
@RequestMapping("/api")
public class MdsController {

    private final Logger log = LoggerFactory.getLogger(TaskResource.class);

    private final MdsService mdsService;

    public MdsController(MdsService mdsService) {
        this.mdsService = mdsService;
    }

    /**
     * {@code GET  /tasks} : get all the offices.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of offices in body.
     */
    @GetMapping("/findAvaliableOffices")
    public List<Office> findAvaliableOffices() {
        log.debug("REST request to get all avaliable Offices");
        return mdsService.findAvaliableOffices();
    }

}
