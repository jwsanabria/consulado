package co.gov.cancilleria.miconsulado.repository.mds;

import co.gov.cancilleria.miconsulado.domain.mds.Office;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Spring Data JPA repository for the {@link Office} entity.
 */
@Repository
@Transactional(value = "mdsTransactionManager", readOnly = true)
@PersistenceContext(name = "mdsEntityManagerFactory")
public interface OfficeRepository extends JpaRepository<Office, Long> {

    @Query("select o from #{#entityName} o where o.status = 1 and o.citizenAttentionCode = 1 and country <> 'COLOMBIA'")
    List<Office> findAvaliable();
}
