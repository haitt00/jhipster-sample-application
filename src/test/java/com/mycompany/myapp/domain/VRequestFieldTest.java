package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VRequestFieldTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VRequestField.class);
        VRequestField vRequestField1 = new VRequestField();
        vRequestField1.setId(1L);
        VRequestField vRequestField2 = new VRequestField();
        vRequestField2.setId(vRequestField1.getId());
        assertThat(vRequestField1).isEqualTo(vRequestField2);
        vRequestField2.setId(2L);
        assertThat(vRequestField1).isNotEqualTo(vRequestField2);
        vRequestField1.setId(null);
        assertThat(vRequestField1).isNotEqualTo(vRequestField2);
    }
}
