package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.VEndpoint;
import com.mycompany.myapp.service.dto.VEndpointDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link VEndpoint} and its DTO {@link VEndpointDTO}.
 */
@Mapper(componentModel = "spring")
public interface VEndpointMapper extends EntityMapper<VEndpointDTO, VEndpoint> {}
