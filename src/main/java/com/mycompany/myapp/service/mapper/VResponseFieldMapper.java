package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.VEndpoint;
import com.mycompany.myapp.domain.VResponseField;
import com.mycompany.myapp.service.dto.VEndpointDTO;
import com.mycompany.myapp.service.dto.VResponseFieldDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link VResponseField} and its DTO {@link VResponseFieldDTO}.
 */
@Mapper(componentModel = "spring")
public interface VResponseFieldMapper extends EntityMapper<VResponseFieldDTO, VResponseField> {
    @Mapping(target = "vEndpoint", source = "vEndpoint", qualifiedByName = "vEndpointId")
    VResponseFieldDTO toDto(VResponseField s);

    @Named("vEndpointId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    VEndpointDTO toDtoVEndpointId(VEndpoint vEndpoint);
}
