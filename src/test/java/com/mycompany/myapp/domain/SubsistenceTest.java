package com.mycompany.myapp.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.mycompany.myapp.web.rest.TestUtil;

public class SubsistenceTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Subsistence.class);
        Subsistence subsistence1 = new Subsistence();
        subsistence1.setId(1L);
        Subsistence subsistence2 = new Subsistence();
        subsistence2.setId(subsistence1.getId());
        assertThat(subsistence1).isEqualTo(subsistence2);
        subsistence2.setId(2L);
        assertThat(subsistence1).isNotEqualTo(subsistence2);
        subsistence1.setId(null);
        assertThat(subsistence1).isNotEqualTo(subsistence2);
    }
}
