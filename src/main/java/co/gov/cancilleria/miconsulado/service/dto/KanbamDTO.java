package co.gov.cancilleria.miconsulado.service.dto;
import java.io.Serializable;
import java.util.Objects;
import co.gov.cancilleria.miconsulado.domain.enumeration.State;

/**
 * A DTO for the {@link co.gov.cancilleria.miconsulado.domain.Kanbam} entity.
 */
public class KanbamDTO implements Serializable {

    private Long id;

    private Long size;

    private State state;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        KanbamDTO kanbamDTO = (KanbamDTO) o;
        if (kanbamDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), kanbamDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "KanbamDTO{" +
            "id=" + getId() +
            ", size=" + getSize() +
            ", state='" + getState() + "'" +
            "}";
    }
}
