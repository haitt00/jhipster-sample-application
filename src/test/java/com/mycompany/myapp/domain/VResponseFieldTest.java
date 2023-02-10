package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VResponseFieldTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VResponseField.class);
        VResponseField vResponseField1 = new VResponseField();
        vResponseField1.setId(1L);
        VResponseField vResponseField2 = new VResponseField();
        vResponseField2.setId(vResponseField1.getId());
        assertThat(vResponseField1).isEqualTo(vResponseField2);
        vResponseField2.setId(2L);
        assertThat(vResponseField1).isNotEqualTo(vResponseField2);
        vResponseField1.setId(null);
        assertThat(vResponseField1).isNotEqualTo(vResponseField2);
    }
}
