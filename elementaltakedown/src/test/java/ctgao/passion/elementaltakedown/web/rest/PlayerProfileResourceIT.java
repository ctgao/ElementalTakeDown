package ctgao.passion.elementaltakedown.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ctgao.passion.elementaltakedown.IntegrationTest;
import ctgao.passion.elementaltakedown.domain.PlayerProfile;
import ctgao.passion.elementaltakedown.repository.PlayerProfileRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PlayerProfileResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PlayerProfileResourceIT {

    private static final Integer DEFAULT_ACTIVE_CARD_IDX = 1;
    private static final Integer UPDATED_ACTIVE_CARD_IDX = 2;

    private static final String ENTITY_API_URL = "/api/player-profiles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PlayerProfileRepository playerProfileRepository;

    @Mock
    private PlayerProfileRepository playerProfileRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPlayerProfileMockMvc;

    private PlayerProfile playerProfile;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PlayerProfile createEntity(EntityManager em) {
        PlayerProfile playerProfile = new PlayerProfile().activeCardIdx(DEFAULT_ACTIVE_CARD_IDX);
        return playerProfile;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PlayerProfile createUpdatedEntity(EntityManager em) {
        PlayerProfile playerProfile = new PlayerProfile().activeCardIdx(UPDATED_ACTIVE_CARD_IDX);
        return playerProfile;
    }

    @BeforeEach
    public void initTest() {
        playerProfile = createEntity(em);
    }

    @Test
    @Transactional
    void createPlayerProfile() throws Exception {
        int databaseSizeBeforeCreate = playerProfileRepository.findAll().size();
        // Create the PlayerProfile
        restPlayerProfileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(playerProfile)))
            .andExpect(status().isCreated());

        // Validate the PlayerProfile in the database
        List<PlayerProfile> playerProfileList = playerProfileRepository.findAll();
        assertThat(playerProfileList).hasSize(databaseSizeBeforeCreate + 1);
        PlayerProfile testPlayerProfile = playerProfileList.get(playerProfileList.size() - 1);
        assertThat(testPlayerProfile.getActiveCardIdx()).isEqualTo(DEFAULT_ACTIVE_CARD_IDX);
    }

    @Test
    @Transactional
    void createPlayerProfileWithExistingId() throws Exception {
        // Create the PlayerProfile with an existing ID
        playerProfile.setId(1L);

        int databaseSizeBeforeCreate = playerProfileRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlayerProfileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(playerProfile)))
            .andExpect(status().isBadRequest());

        // Validate the PlayerProfile in the database
        List<PlayerProfile> playerProfileList = playerProfileRepository.findAll();
        assertThat(playerProfileList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPlayerProfiles() throws Exception {
        // Initialize the database
        playerProfileRepository.saveAndFlush(playerProfile);

        // Get all the playerProfileList
        restPlayerProfileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(playerProfile.getId().intValue())))
            .andExpect(jsonPath("$.[*].activeCardIdx").value(hasItem(DEFAULT_ACTIVE_CARD_IDX)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPlayerProfilesWithEagerRelationshipsIsEnabled() throws Exception {
        when(playerProfileRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPlayerProfileMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(playerProfileRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPlayerProfilesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(playerProfileRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPlayerProfileMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(playerProfileRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPlayerProfile() throws Exception {
        // Initialize the database
        playerProfileRepository.saveAndFlush(playerProfile);

        // Get the playerProfile
        restPlayerProfileMockMvc
            .perform(get(ENTITY_API_URL_ID, playerProfile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(playerProfile.getId().intValue()))
            .andExpect(jsonPath("$.activeCardIdx").value(DEFAULT_ACTIVE_CARD_IDX));
    }

    @Test
    @Transactional
    void getNonExistingPlayerProfile() throws Exception {
        // Get the playerProfile
        restPlayerProfileMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPlayerProfile() throws Exception {
        // Initialize the database
        playerProfileRepository.saveAndFlush(playerProfile);

        int databaseSizeBeforeUpdate = playerProfileRepository.findAll().size();

        // Update the playerProfile
        PlayerProfile updatedPlayerProfile = playerProfileRepository.findById(playerProfile.getId()).get();
        // Disconnect from session so that the updates on updatedPlayerProfile are not directly saved in db
        em.detach(updatedPlayerProfile);
        updatedPlayerProfile.activeCardIdx(UPDATED_ACTIVE_CARD_IDX);

        restPlayerProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPlayerProfile.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPlayerProfile))
            )
            .andExpect(status().isOk());

        // Validate the PlayerProfile in the database
        List<PlayerProfile> playerProfileList = playerProfileRepository.findAll();
        assertThat(playerProfileList).hasSize(databaseSizeBeforeUpdate);
        PlayerProfile testPlayerProfile = playerProfileList.get(playerProfileList.size() - 1);
        assertThat(testPlayerProfile.getActiveCardIdx()).isEqualTo(UPDATED_ACTIVE_CARD_IDX);
    }

    @Test
    @Transactional
    void putNonExistingPlayerProfile() throws Exception {
        int databaseSizeBeforeUpdate = playerProfileRepository.findAll().size();
        playerProfile.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlayerProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, playerProfile.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(playerProfile))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlayerProfile in the database
        List<PlayerProfile> playerProfileList = playerProfileRepository.findAll();
        assertThat(playerProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPlayerProfile() throws Exception {
        int databaseSizeBeforeUpdate = playerProfileRepository.findAll().size();
        playerProfile.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlayerProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(playerProfile))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlayerProfile in the database
        List<PlayerProfile> playerProfileList = playerProfileRepository.findAll();
        assertThat(playerProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPlayerProfile() throws Exception {
        int databaseSizeBeforeUpdate = playerProfileRepository.findAll().size();
        playerProfile.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlayerProfileMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(playerProfile)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PlayerProfile in the database
        List<PlayerProfile> playerProfileList = playerProfileRepository.findAll();
        assertThat(playerProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePlayerProfileWithPatch() throws Exception {
        // Initialize the database
        playerProfileRepository.saveAndFlush(playerProfile);

        int databaseSizeBeforeUpdate = playerProfileRepository.findAll().size();

        // Update the playerProfile using partial update
        PlayerProfile partialUpdatedPlayerProfile = new PlayerProfile();
        partialUpdatedPlayerProfile.setId(playerProfile.getId());

        restPlayerProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlayerProfile.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPlayerProfile))
            )
            .andExpect(status().isOk());

        // Validate the PlayerProfile in the database
        List<PlayerProfile> playerProfileList = playerProfileRepository.findAll();
        assertThat(playerProfileList).hasSize(databaseSizeBeforeUpdate);
        PlayerProfile testPlayerProfile = playerProfileList.get(playerProfileList.size() - 1);
        assertThat(testPlayerProfile.getActiveCardIdx()).isEqualTo(DEFAULT_ACTIVE_CARD_IDX);
    }

    @Test
    @Transactional
    void fullUpdatePlayerProfileWithPatch() throws Exception {
        // Initialize the database
        playerProfileRepository.saveAndFlush(playerProfile);

        int databaseSizeBeforeUpdate = playerProfileRepository.findAll().size();

        // Update the playerProfile using partial update
        PlayerProfile partialUpdatedPlayerProfile = new PlayerProfile();
        partialUpdatedPlayerProfile.setId(playerProfile.getId());

        partialUpdatedPlayerProfile.activeCardIdx(UPDATED_ACTIVE_CARD_IDX);

        restPlayerProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlayerProfile.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPlayerProfile))
            )
            .andExpect(status().isOk());

        // Validate the PlayerProfile in the database
        List<PlayerProfile> playerProfileList = playerProfileRepository.findAll();
        assertThat(playerProfileList).hasSize(databaseSizeBeforeUpdate);
        PlayerProfile testPlayerProfile = playerProfileList.get(playerProfileList.size() - 1);
        assertThat(testPlayerProfile.getActiveCardIdx()).isEqualTo(UPDATED_ACTIVE_CARD_IDX);
    }

    @Test
    @Transactional
    void patchNonExistingPlayerProfile() throws Exception {
        int databaseSizeBeforeUpdate = playerProfileRepository.findAll().size();
        playerProfile.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlayerProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, playerProfile.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(playerProfile))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlayerProfile in the database
        List<PlayerProfile> playerProfileList = playerProfileRepository.findAll();
        assertThat(playerProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPlayerProfile() throws Exception {
        int databaseSizeBeforeUpdate = playerProfileRepository.findAll().size();
        playerProfile.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlayerProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(playerProfile))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlayerProfile in the database
        List<PlayerProfile> playerProfileList = playerProfileRepository.findAll();
        assertThat(playerProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPlayerProfile() throws Exception {
        int databaseSizeBeforeUpdate = playerProfileRepository.findAll().size();
        playerProfile.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlayerProfileMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(playerProfile))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PlayerProfile in the database
        List<PlayerProfile> playerProfileList = playerProfileRepository.findAll();
        assertThat(playerProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePlayerProfile() throws Exception {
        // Initialize the database
        playerProfileRepository.saveAndFlush(playerProfile);

        int databaseSizeBeforeDelete = playerProfileRepository.findAll().size();

        // Delete the playerProfile
        restPlayerProfileMockMvc
            .perform(delete(ENTITY_API_URL_ID, playerProfile.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PlayerProfile> playerProfileList = playerProfileRepository.findAll();
        assertThat(playerProfileList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
