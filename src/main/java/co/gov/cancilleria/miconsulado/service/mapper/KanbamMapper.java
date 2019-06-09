package co.gov.cancilleria.miconsulado.service.mapper;

import co.gov.cancilleria.miconsulado.domain.*;
import co.gov.cancilleria.miconsulado.service.dto.KanbamDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Kanbam} and its DTO {@link KanbamDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface KanbamMapper extends EntityMapper<KanbamDTO, Kanbam> {


    @Mapping(target = "owners", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    Kanbam toEntity(KanbamDTO kanbamDTO);

    default Kanbam fromId(Long id) {
        if (id == null) {
            return null;
        }
        Kanbam kanbam = new Kanbam();
        kanbam.setId(id);
        return kanbam;
    }
}
