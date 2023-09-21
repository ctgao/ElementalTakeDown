package ctgao.passion.elementaltakedown.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ctgao.passion.elementaltakedown.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PlayerProfileTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlayerProfile.class);
        PlayerProfile playerProfile1 = new PlayerProfile();
        playerProfile1.setId(1L);
        PlayerProfile playerProfile2 = new PlayerProfile();
        playerProfile2.setId(playerProfile1.getId());
        assertThat(playerProfile1).isEqualTo(playerProfile2);
        playerProfile2.setId(2L);
        assertThat(playerProfile1).isNotEqualTo(playerProfile2);
        playerProfile1.setId(null);
        assertThat(playerProfile1).isNotEqualTo(playerProfile2);
    }
}
