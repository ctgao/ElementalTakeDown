package ctgao.passion.elementaltakedown.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ctgao.passion.elementaltakedown.IntegrationTest;
import ctgao.passion.elementaltakedown.domain.GameTable;
import ctgao.passion.elementaltakedown.repository.GameTableRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link GameTableResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GameTableResourceIT {

    private static final Boolean DEFAULT_P_1_TURN = false;
    private static final Boolean UPDATED_P_1_TURN = true;

    private static final String ENTITY_API_URL = "/api/game-tables";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GameTableRepository gameTableRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGameTableMockMvc;

    private GameTable gameTable;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GameTable createEntity(EntityManager em) {
        GameTable gameTable = new GameTable().p1turn(DEFAULT_P_1_TURN);
        return gameTable;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GameTable createUpdatedEntity(EntityManager em) {
        GameTable gameTable = new GameTable().p1turn(UPDATED_P_1_TURN);
        return gameTable;
    }

    @BeforeEach
    public void initTest() {
        gameTable = createEntity(em);
    }

    @Test
    @Transactional
    void createGameTable() throws Exception {
        int databaseSizeBeforeCreate = gameTableRepository.findAll().size();
        // Create the GameTable
        restGameTableMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gameTable)))
            .andExpect(status().isCreated());

        // Validate the GameTable in the database
        List<GameTable> gameTableList = gameTableRepository.findAll();
        assertThat(gameTableList).hasSize(databaseSizeBeforeCreate + 1);
        GameTable testGameTable = gameTableList.get(gameTableList.size() - 1);
        assertThat(testGameTable.getp1turn()).isEqualTo(DEFAULT_P_1_TURN);
    }

    @Test
    @Transactional
    void createGameTableWithExistingId() throws Exception {
        // Create the GameTable with an existing ID
        gameTable.setId(1L);

        int databaseSizeBeforeCreate = gameTableRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGameTableMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gameTable)))
            .andExpect(status().isBadRequest());

        // Validate the GameTable in the database
        List<GameTable> gameTableList = gameTableRepository.findAll();
        assertThat(gameTableList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllGameTables() throws Exception {
        // Initialize the database
        gameTableRepository.saveAndFlush(gameTable);

        // Get all the gameTableList
        restGameTableMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gameTable.getId().intValue())))
            .andExpect(jsonPath("$.[*].p1turn").value(hasItem(DEFAULT_P_1_TURN.booleanValue())));
    }

    @Test
    @Transactional
    void getGameTable() throws Exception {
        // Initialize the database
        gameTableRepository.saveAndFlush(gameTable);

        // Get the gameTable
        restGameTableMockMvc
            .perform(get(ENTITY_API_URL_ID, gameTable.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(gameTable.getId().intValue()))
            .andExpect(jsonPath("$.p1turn").value(DEFAULT_P_1_TURN.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingGameTable() throws Exception {
        // Get the gameTable
        restGameTableMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingGameTable() throws Exception {
        // Initialize the database
        gameTableRepository.saveAndFlush(gameTable);

        int databaseSizeBeforeUpdate = gameTableRepository.findAll().size();

        // Update the gameTable
        GameTable updatedGameTable = gameTableRepository.findById(gameTable.getId()).get();
        // Disconnect from session so that the updates on updatedGameTable are not directly saved in db
        em.detach(updatedGameTable);
        updatedGameTable.p1turn(UPDATED_P_1_TURN);

        restGameTableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedGameTable.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedGameTable))
            )
            .andExpect(status().isOk());

        // Validate the GameTable in the database
        List<GameTable> gameTableList = gameTableRepository.findAll();
        assertThat(gameTableList).hasSize(databaseSizeBeforeUpdate);
        GameTable testGameTable = gameTableList.get(gameTableList.size() - 1);
        assertThat(testGameTable.getp1turn()).isEqualTo(UPDATED_P_1_TURN);
    }

    @Test
    @Transactional
    void putNonExistingGameTable() throws Exception {
        int databaseSizeBeforeUpdate = gameTableRepository.findAll().size();
        gameTable.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGameTableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, gameTable.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gameTable))
            )
            .andExpect(status().isBadRequest());

        // Validate the GameTable in the database
        List<GameTable> gameTableList = gameTableRepository.findAll();
        assertThat(gameTableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGameTable() throws Exception {
        int databaseSizeBeforeUpdate = gameTableRepository.findAll().size();
        gameTable.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGameTableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gameTable))
            )
            .andExpect(status().isBadRequest());

        // Validate the GameTable in the database
        List<GameTable> gameTableList = gameTableRepository.findAll();
        assertThat(gameTableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGameTable() throws Exception {
        int databaseSizeBeforeUpdate = gameTableRepository.findAll().size();
        gameTable.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGameTableMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gameTable)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the GameTable in the database
        List<GameTable> gameTableList = gameTableRepository.findAll();
        assertThat(gameTableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGameTableWithPatch() throws Exception {
        // Initialize the database
        gameTableRepository.saveAndFlush(gameTable);

        int databaseSizeBeforeUpdate = gameTableRepository.findAll().size();

        // Update the gameTable using partial update
        GameTable partialUpdatedGameTable = new GameTable();
        partialUpdatedGameTable.setId(gameTable.getId());

        restGameTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGameTable.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGameTable))
            )
            .andExpect(status().isOk());

        // Validate the GameTable in the database
        List<GameTable> gameTableList = gameTableRepository.findAll();
        assertThat(gameTableList).hasSize(databaseSizeBeforeUpdate);
        GameTable testGameTable = gameTableList.get(gameTableList.size() - 1);
        assertThat(testGameTable.getp1turn()).isEqualTo(DEFAULT_P_1_TURN);
    }

    @Test
    @Transactional
    void fullUpdateGameTableWithPatch() throws Exception {
        // Initialize the database
        gameTableRepository.saveAndFlush(gameTable);

        int databaseSizeBeforeUpdate = gameTableRepository.findAll().size();

        // Update the gameTable using partial update
        GameTable partialUpdatedGameTable = new GameTable();
        partialUpdatedGameTable.setId(gameTable.getId());

        partialUpdatedGameTable.p1turn(UPDATED_P_1_TURN);

        restGameTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGameTable.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGameTable))
            )
            .andExpect(status().isOk());

        // Validate the GameTable in the database
        List<GameTable> gameTableList = gameTableRepository.findAll();
        assertThat(gameTableList).hasSize(databaseSizeBeforeUpdate);
        GameTable testGameTable = gameTableList.get(gameTableList.size() - 1);
        assertThat(testGameTable.getp1turn()).isEqualTo(UPDATED_P_1_TURN);
    }

    @Test
    @Transactional
    void patchNonExistingGameTable() throws Exception {
        int databaseSizeBeforeUpdate = gameTableRepository.findAll().size();
        gameTable.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGameTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, gameTable.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gameTable))
            )
            .andExpect(status().isBadRequest());

        // Validate the GameTable in the database
        List<GameTable> gameTableList = gameTableRepository.findAll();
        assertThat(gameTableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGameTable() throws Exception {
        int databaseSizeBeforeUpdate = gameTableRepository.findAll().size();
        gameTable.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGameTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gameTable))
            )
            .andExpect(status().isBadRequest());

        // Validate the GameTable in the database
        List<GameTable> gameTableList = gameTableRepository.findAll();
        assertThat(gameTableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGameTable() throws Exception {
        int databaseSizeBeforeUpdate = gameTableRepository.findAll().size();
        gameTable.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGameTableMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(gameTable))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the GameTable in the database
        List<GameTable> gameTableList = gameTableRepository.findAll();
        assertThat(gameTableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGameTable() throws Exception {
        // Initialize the database
        gameTableRepository.saveAndFlush(gameTable);

        int databaseSizeBeforeDelete = gameTableRepository.findAll().size();

        // Delete the gameTable
        restGameTableMockMvc
            .perform(delete(ENTITY_API_URL_ID, gameTable.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<GameTable> gameTableList = gameTableRepository.findAll();
        assertThat(gameTableList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
