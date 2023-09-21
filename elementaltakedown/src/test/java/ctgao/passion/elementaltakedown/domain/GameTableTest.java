package ctgao.passion.elementaltakedown.domain;

import static org.assertj.core.api.Assertions.assertThat;

import ctgao.passion.elementaltakedown.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GameTableTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GameTable.class);
        GameTable gameTable1 = new GameTable();
        gameTable1.setId(1L);
        GameTable gameTable2 = new GameTable();
        gameTable2.setId(gameTable1.getId());
        assertThat(gameTable1).isEqualTo(gameTable2);
        gameTable2.setId(2L);
        assertThat(gameTable1).isNotEqualTo(gameTable2);
        gameTable1.setId(null);
        assertThat(gameTable1).isNotEqualTo(gameTable2);
    }
}
