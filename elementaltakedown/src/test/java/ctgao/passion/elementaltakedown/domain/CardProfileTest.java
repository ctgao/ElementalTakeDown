package ctgao.passion.elementaltakedown.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ctgao.passion.elementaltakedown.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CardProfileTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CardProfile.class);
        CardProfile cardProfile1 = new CardProfile();
        cardProfile1.setId(1L);
        CardProfile cardProfile2 = new CardProfile();
        cardProfile2.setId(cardProfile1.getId());
        assertThat(cardProfile1).isEqualTo(cardProfile2);
        cardProfile2.setId(2L);
        assertThat(cardProfile1).isNotEqualTo(cardProfile2);
        cardProfile1.setId(null);
        assertThat(cardProfile1).isNotEqualTo(cardProfile2);
    }
}
