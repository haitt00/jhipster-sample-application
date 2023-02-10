package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VEndpointMapperTest {

    private VEndpointMapper vEndpointMapper;

    @BeforeEach
    public void setUp() {
        vEndpointMapper = new VEndpointMapperImpl();
    }
}
