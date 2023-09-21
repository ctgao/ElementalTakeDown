package ctgao.passion.elementaltakedown.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ctgao.passion.elementaltakedown.IntegrationTest;
import ctgao.passion.elementaltakedown.domain.CardProfile;
import ctgao.passion.elementaltakedown.domain.enumeration.DmgElementType;
import ctgao.passion.elementaltakedown.repository.CardProfileRepository;
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
 * Integration tests for the {@link CardProfileResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CardProfileResourceIT {

    private static final Integer DEFAULT_CURRENT_HP = 0;
    private static final Integer UPDATED_CURRENT_HP = 1;

    private static final Boolean DEFAULT_INFUSION = false;
    private static final Boolean UPDATED_INFUSION = true;

    private static final DmgElementType DEFAULT_ELEMENTAL_STATUS = DmgElementType.WATER;
    private static final DmgElementType UPDATED_ELEMENTAL_STATUS = DmgElementType.FIRE;

    private static final String ENTITY_API_URL = "/api/card-profiles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CardProfileRepository cardProfileRepository;

    @Mock
    private CardProfileRepository cardProfileRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCardProfileMockMvc;

    private CardProfile cardProfile;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CardProfile createEntity(EntityManager em) {
        CardProfile cardProfile = new CardProfile()
            .currentHP(DEFAULT_CURRENT_HP)
            .infusion(DEFAULT_INFUSION)
            .elementalStatus(DEFAULT_ELEMENTAL_STATUS);
        return cardProfile;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CardProfile createUpdatedEntity(EntityManager em) {
        CardProfile cardProfile = new CardProfile()
            .currentHP(UPDATED_CURRENT_HP)
            .infusion(UPDATED_INFUSION)
            .elementalStatus(UPDATED_ELEMENTAL_STATUS);
        return cardProfile;
    }

    @BeforeEach
    public void initTest() {
        cardProfile = createEntity(em);
    }

    @Test
    @Transactional
    void createCardProfile() throws Exception {
        int databaseSizeBeforeCreate = cardProfileRepository.findAll().size();
        // Create the CardProfile
        restCardProfileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cardProfile)))
            .andExpect(status().isCreated());

        // Validate the CardProfile in the database
        List<CardProfile> cardProfileList = cardProfileRepository.findAll();
        assertThat(cardProfileList).hasSize(databaseSizeBeforeCreate + 1);
        CardProfile testCardProfile = cardProfileList.get(cardProfileList.size() - 1);
        assertThat(testCardProfile.getCurrentHP()).isEqualTo(DEFAULT_CURRENT_HP);
        assertThat(testCardProfile.getInfusion()).isEqualTo(DEFAULT_INFUSION);
        assertThat(testCardProfile.getElementalStatus()).isEqualTo(DEFAULT_ELEMENTAL_STATUS);
    }

    @Test
    @Transactional
    void createCardProfileWithExistingId() throws Exception {
        // Create the CardProfile with an existing ID
        cardProfile.setId(1L);

        int databaseSizeBeforeCreate = cardProfileRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCardProfileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cardProfile)))
            .andExpect(status().isBadRequest());

        // Validate the CardProfile in the database
        List<CardProfile> cardProfileList = cardProfileRepository.findAll();
        assertThat(cardProfileList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCurrentHPIsRequired() throws Exception {
        int databaseSizeBeforeTest = cardProfileRepository.findAll().size();
        // set the field null
        cardProfile.setCurrentHP(null);

        // Create the CardProfile, which fails.

        restCardProfileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cardProfile)))
            .andExpect(status().isBadRequest());

        List<CardProfile> cardProfileList = cardProfileRepository.findAll();
        assertThat(cardProfileList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkInfusionIsRequired() throws Exception {
        int databaseSizeBeforeTest = cardProfileRepository.findAll().size();
        // set the field null
        cardProfile.setInfusion(null);

        // Create the CardProfile, which fails.

        restCardProfileMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cardProfile)))
            .andExpect(status().isBadRequest());

        List<CardProfile> cardProfileList = cardProfileRepository.findAll();
        assertThat(cardProfileList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCardProfiles() throws Exception {
        // Initialize the database
        cardProfileRepository.saveAndFlush(cardProfile);

        // Get all the cardProfileList
        restCardProfileMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cardProfile.getId().intValue())))
            .andExpect(jsonPath("$.[*].currentHP").value(hasItem(DEFAULT_CURRENT_HP)))
            .andExpect(jsonPath("$.[*].infusion").value(hasItem(DEFAULT_INFUSION.booleanValue())))
            .andExpect(jsonPath("$.[*].elementalStatus").value(hasItem(DEFAULT_ELEMENTAL_STATUS.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCardProfilesWithEagerRelationshipsIsEnabled() throws Exception {
        when(cardProfileRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCardProfileMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(cardProfileRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCardProfilesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(cardProfileRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCardProfileMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(cardProfileRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCardProfile() throws Exception {
        // Initialize the database
        cardProfileRepository.saveAndFlush(cardProfile);

        // Get the cardProfile
        restCardProfileMockMvc
            .perform(get(ENTITY_API_URL_ID, cardProfile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cardProfile.getId().intValue()))
            .andExpect(jsonPath("$.currentHP").value(DEFAULT_CURRENT_HP))
            .andExpect(jsonPath("$.infusion").value(DEFAULT_INFUSION.booleanValue()))
            .andExpect(jsonPath("$.elementalStatus").value(DEFAULT_ELEMENTAL_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingCardProfile() throws Exception {
        // Get the cardProfile
        restCardProfileMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCardProfile() throws Exception {
        // Initialize the database
        cardProfileRepository.saveAndFlush(cardProfile);

        int databaseSizeBeforeUpdate = cardProfileRepository.findAll().size();

        // Update the cardProfile
        CardProfile updatedCardProfile = cardProfileRepository.findById(cardProfile.getId()).get();
        // Disconnect from session so that the updates on updatedCardProfile are not directly saved in db
        em.detach(updatedCardProfile);
        updatedCardProfile.currentHP(UPDATED_CURRENT_HP).infusion(UPDATED_INFUSION).elementalStatus(UPDATED_ELEMENTAL_STATUS);

        restCardProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCardProfile.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCardProfile))
            )
            .andExpect(status().isOk());

        // Validate the CardProfile in the database
        List<CardProfile> cardProfileList = cardProfileRepository.findAll();
        assertThat(cardProfileList).hasSize(databaseSizeBeforeUpdate);
        CardProfile testCardProfile = cardProfileList.get(cardProfileList.size() - 1);
        assertThat(testCardProfile.getCurrentHP()).isEqualTo(UPDATED_CURRENT_HP);
        assertThat(testCardProfile.getInfusion()).isEqualTo(UPDATED_INFUSION);
        assertThat(testCardProfile.getElementalStatus()).isEqualTo(UPDATED_ELEMENTAL_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingCardProfile() throws Exception {
        int databaseSizeBeforeUpdate = cardProfileRepository.findAll().size();
        cardProfile.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCardProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cardProfile.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardProfile))
            )
            .andExpect(status().isBadRequest());

        // Validate the CardProfile in the database
        List<CardProfile> cardProfileList = cardProfileRepository.findAll();
        assertThat(cardProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCardProfile() throws Exception {
        int databaseSizeBeforeUpdate = cardProfileRepository.findAll().size();
        cardProfile.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardProfileMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cardProfile))
            )
            .andExpect(status().isBadRequest());

        // Validate the CardProfile in the database
        List<CardProfile> cardProfileList = cardProfileRepository.findAll();
        assertThat(cardProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCardProfile() throws Exception {
        int databaseSizeBeforeUpdate = cardProfileRepository.findAll().size();
        cardProfile.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardProfileMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cardProfile)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CardProfile in the database
        List<CardProfile> cardProfileList = cardProfileRepository.findAll();
        assertThat(cardProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCardProfileWithPatch() throws Exception {
        // Initialize the database
        cardProfileRepository.saveAndFlush(cardProfile);

        int databaseSizeBeforeUpdate = cardProfileRepository.findAll().size();

        // Update the cardProfile using partial update
        CardProfile partialUpdatedCardProfile = new CardProfile();
        partialUpdatedCardProfile.setId(cardProfile.getId());

        restCardProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCardProfile.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCardProfile))
            )
            .andExpect(status().isOk());

        // Validate the CardProfile in the database
        List<CardProfile> cardProfileList = cardProfileRepository.findAll();
        assertThat(cardProfileList).hasSize(databaseSizeBeforeUpdate);
        CardProfile testCardProfile = cardProfileList.get(cardProfileList.size() - 1);
        assertThat(testCardProfile.getCurrentHP()).isEqualTo(DEFAULT_CURRENT_HP);
        assertThat(testCardProfile.getInfusion()).isEqualTo(DEFAULT_INFUSION);
        assertThat(testCardProfile.getElementalStatus()).isEqualTo(DEFAULT_ELEMENTAL_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateCardProfileWithPatch() throws Exception {
        // Initialize the database
        cardProfileRepository.saveAndFlush(cardProfile);

        int databaseSizeBeforeUpdate = cardProfileRepository.findAll().size();

        // Update the cardProfile using partial update
        CardProfile partialUpdatedCardProfile = new CardProfile();
        partialUpdatedCardProfile.setId(cardProfile.getId());

        partialUpdatedCardProfile.currentHP(UPDATED_CURRENT_HP).infusion(UPDATED_INFUSION).elementalStatus(UPDATED_ELEMENTAL_STATUS);

        restCardProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCardProfile.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCardProfile))
            )
            .andExpect(status().isOk());

        // Validate the CardProfile in the database
        List<CardProfile> cardProfileList = cardProfileRepository.findAll();
        assertThat(cardProfileList).hasSize(databaseSizeBeforeUpdate);
        CardProfile testCardProfile = cardProfileList.get(cardProfileList.size() - 1);
        assertThat(testCardProfile.getCurrentHP()).isEqualTo(UPDATED_CURRENT_HP);
        assertThat(testCardProfile.getInfusion()).isEqualTo(UPDATED_INFUSION);
        assertThat(testCardProfile.getElementalStatus()).isEqualTo(UPDATED_ELEMENTAL_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingCardProfile() throws Exception {
        int databaseSizeBeforeUpdate = cardProfileRepository.findAll().size();
        cardProfile.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCardProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cardProfile.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cardProfile))
            )
            .andExpect(status().isBadRequest());

        // Validate the CardProfile in the database
        List<CardProfile> cardProfileList = cardProfileRepository.findAll();
        assertThat(cardProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCardProfile() throws Exception {
        int databaseSizeBeforeUpdate = cardProfileRepository.findAll().size();
        cardProfile.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardProfileMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cardProfile))
            )
            .andExpect(status().isBadRequest());

        // Validate the CardProfile in the database
        List<CardProfile> cardProfileList = cardProfileRepository.findAll();
        assertThat(cardProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCardProfile() throws Exception {
        int databaseSizeBeforeUpdate = cardProfileRepository.findAll().size();
        cardProfile.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCardProfileMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(cardProfile))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CardProfile in the database
        List<CardProfile> cardProfileList = cardProfileRepository.findAll();
        assertThat(cardProfileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCardProfile() throws Exception {
        // Initialize the database
        cardProfileRepository.saveAndFlush(cardProfile);

        int databaseSizeBeforeDelete = cardProfileRepository.findAll().size();

        // Delete the cardProfile
        restCardProfileMockMvc
            .perform(delete(ENTITY_API_URL_ID, cardProfile.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CardProfile> cardProfileList = cardProfileRepository.findAll();
        assertThat(cardProfileList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
