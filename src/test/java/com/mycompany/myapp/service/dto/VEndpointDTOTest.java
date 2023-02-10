package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VEndpointDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VEndpointDTO.class);
        VEndpointDTO vEndpointDTO1 = new VEndpointDTO();
        vEndpointDTO1.setId(1L);
        VEndpointDTO vEndpointDTO2 = new VEndpointDTO();
        assertThat(vEndpointDTO1).isNotEqualTo(vEndpointDTO2);
        vEndpointDTO2.setId(vEndpointDTO1.getId());
        assertThat(vEndpointDTO1).isEqualTo(vEndpointDTO2);
        vEndpointDTO2.setId(2L);
        assertThat(vEndpointDTO1).isNotEqualTo(vEndpointDTO2);
        vEndpointDTO1.setId(null);
        assertThat(vEndpointDTO1).isNotEqualTo(vEndpointDTO2);
    }
}
