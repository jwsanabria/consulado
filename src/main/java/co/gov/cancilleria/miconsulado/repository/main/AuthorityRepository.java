package co.gov.cancilleria.miconsulado.repository.main;

import co.gov.cancilleria.miconsulado.domain.main.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {
}
