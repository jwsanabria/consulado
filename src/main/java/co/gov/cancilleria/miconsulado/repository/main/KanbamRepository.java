package co.gov.cancilleria.miconsulado.repository.main;

import co.gov.cancilleria.miconsulado.domain.main.Kanbam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Kanbam entity.
 */
@SuppressWarnings("unused")
@Repository
public interface KanbamRepository extends JpaRepository<Kanbam, Long> {

}
