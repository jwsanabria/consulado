package co.gov.cancilleria.miconsulado.web.rest;

import co.gov.cancilleria.miconsulado.service.KanbamService;
import co.gov.cancilleria.miconsulado.web.rest.errors.BadRequestAlertException;
import co.gov.cancilleria.miconsulado.service.dto.KanbamDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link co.gov.cancilleria.miconsulado.domain.Kanbam}.
 */
@RestController
@RequestMapping("/api")
public class KanbamResource {

    private final Logger log = LoggerFactory.getLogger(KanbamResource.class);

    private static final String ENTITY_NAME = "kanbam";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final KanbamService kanbamService;

    public KanbamResource(KanbamService kanbamService) {
        this.kanbamService = kanbamService;
    }

    /**
     * {@code POST  /kanbams} : Create a new kanbam.
     *
     * @param kanbamDTO the kanbamDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new kanbamDTO, or with status {@code 400 (Bad Request)} if the kanbam has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/kanbams")
    public ResponseEntity<KanbamDTO> createKanbam(@RequestBody KanbamDTO kanbamDTO) throws URISyntaxException {
        log.debug("REST request to save Kanbam : {}", kanbamDTO);
        if (kanbamDTO.getId() != null) {
            throw new BadRequestAlertException("A new kanbam cannot already have an ID", ENTITY_NAME, "idexists");
        }
        KanbamDTO result = kanbamService.save(kanbamDTO);
        return ResponseEntity.created(new URI("/api/kanbams/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /kanbams} : Updates an existing kanbam.
     *
     * @param kanbamDTO the kanbamDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated kanbamDTO,
     * or with status {@code 400 (Bad Request)} if the kanbamDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the kanbamDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/kanbams")
    public ResponseEntity<KanbamDTO> updateKanbam(@RequestBody KanbamDTO kanbamDTO) throws URISyntaxException {
        log.debug("REST request to update Kanbam : {}", kanbamDTO);
        if (kanbamDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        KanbamDTO result = kanbamService.save(kanbamDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, kanbamDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /kanbams} : get all the kanbams.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of kanbams in body.
     */
    @GetMapping("/kanbams")
    public List<KanbamDTO> getAllKanbams() {
        log.debug("REST request to get all Kanbams");
        return kanbamService.findAll();
    }

    /**
     * {@code GET  /kanbams/:id} : get the "id" kanbam.
     *
     * @param id the id of the kanbamDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the kanbamDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/kanbams/{id}")
    public ResponseEntity<KanbamDTO> getKanbam(@PathVariable Long id) {
        log.debug("REST request to get Kanbam : {}", id);
        Optional<KanbamDTO> kanbamDTO = kanbamService.findOne(id);
        return ResponseUtil.wrapOrNotFound(kanbamDTO);
    }

    /**
     * {@code DELETE  /kanbams/:id} : delete the "id" kanbam.
     *
     * @param id the id of the kanbamDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/kanbams/{id}")
    public ResponseEntity<Void> deleteKanbam(@PathVariable Long id) {
        log.debug("REST request to delete Kanbam : {}", id);
        kanbamService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
