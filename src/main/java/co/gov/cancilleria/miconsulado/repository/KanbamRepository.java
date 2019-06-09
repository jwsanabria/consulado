package co.gov.cancilleria.miconsulado.repository;

import co.gov.cancilleria.miconsulado.domain.Kanbam;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Kanbam entity.
 */
@SuppressWarnings("unused")
@Repository
public interface KanbamRepository extends JpaRepository<Kanbam, Long> {

}
