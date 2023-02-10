package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VResponseFieldDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VResponseFieldDTO.class);
        VResponseFieldDTO vResponseFieldDTO1 = new VResponseFieldDTO();
        vResponseFieldDTO1.setId(1L);
        VResponseFieldDTO vResponseFieldDTO2 = new VResponseFieldDTO();
        assertThat(vResponseFieldDTO1).isNotEqualTo(vResponseFieldDTO2);
        vResponseFieldDTO2.setId(vResponseFieldDTO1.getId());
        assertThat(vResponseFieldDTO1).isEqualTo(vResponseFieldDTO2);
        vResponseFieldDTO2.setId(2L);
        assertThat(vResponseFieldDTO1).isNotEqualTo(vResponseFieldDTO2);
        vResponseFieldDTO1.setId(null);
        assertThat(vResponseFieldDTO1).isNotEqualTo(vResponseFieldDTO2);
    }
}
