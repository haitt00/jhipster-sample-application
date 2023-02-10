package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.VEndpoint;
import com.mycompany.myapp.domain.VRequestField;
import com.mycompany.myapp.service.dto.VEndpointDTO;
import com.mycompany.myapp.service.dto.VRequestFieldDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link VRequestField} and its DTO {@link VRequestFieldDTO}.
 */
@Mapper(componentModel = "spring")
public interface VRequestFieldMapper extends EntityMapper<VRequestFieldDTO, VRequestField> {
    @Mapping(target = "vEndpoint", source = "vEndpoint", qualifiedByName = "vEndpointId")
    VRequestFieldDTO toDto(VRequestField s);

    @Named("vEndpointId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    VEndpointDTO toDtoVEndpointId(VEndpoint vEndpoint);
}
