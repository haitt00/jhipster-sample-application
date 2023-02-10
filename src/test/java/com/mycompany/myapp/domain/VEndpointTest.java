package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VEndpointTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VEndpoint.class);
        VEndpoint vEndpoint1 = new VEndpoint();
        vEndpoint1.setId(1L);
        VEndpoint vEndpoint2 = new VEndpoint();
        vEndpoint2.setId(vEndpoint1.getId());
        assertThat(vEndpoint1).isEqualTo(vEndpoint2);
        vEndpoint2.setId(2L);
        assertThat(vEndpoint1).isNotEqualTo(vEndpoint2);
        vEndpoint1.setId(null);
        assertThat(vEndpoint1).isNotEqualTo(vEndpoint2);
    }
}
