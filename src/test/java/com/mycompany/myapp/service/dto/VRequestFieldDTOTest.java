package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VRequestFieldDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(VRequestFieldDTO.class);
        VRequestFieldDTO vRequestFieldDTO1 = new VRequestFieldDTO();
        vRequestFieldDTO1.setId(1L);
        VRequestFieldDTO vRequestFieldDTO2 = new VRequestFieldDTO();
        assertThat(vRequestFieldDTO1).isNotEqualTo(vRequestFieldDTO2);
        vRequestFieldDTO2.setId(vRequestFieldDTO1.getId());
        assertThat(vRequestFieldDTO1).isEqualTo(vRequestFieldDTO2);
        vRequestFieldDTO2.setId(2L);
        assertThat(vRequestFieldDTO1).isNotEqualTo(vRequestFieldDTO2);
        vRequestFieldDTO1.setId(null);
        assertThat(vRequestFieldDTO1).isNotEqualTo(vRequestFieldDTO2);
    }
}
