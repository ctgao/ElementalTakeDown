package ctgao.passion.elementaltakedown.web.rest;

import ctgao.passion.elementaltakedown.domain.PlayerProfile;
import ctgao.passion.elementaltakedown.repository.PlayerProfileRepository;
import ctgao.passion.elementaltakedown.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ctgao.passion.elementaltakedown.domain.PlayerProfile}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PlayerProfileResource {

    private final Logger log = LoggerFactory.getLogger(PlayerProfileResource.class);

    private static final String ENTITY_NAME = "playerProfile";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PlayerProfileRepository playerProfileRepository;

    public PlayerProfileResource(PlayerProfileRepository playerProfileRepository) {
        this.playerProfileRepository = playerProfileRepository;
    }

    /**
     * {@code POST  /player-profiles} : Create a new playerProfile.
     *
     * @param playerProfile the playerProfile to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new playerProfile, or with status {@code 400 (Bad Request)} if the playerProfile has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/player-profiles")
    public ResponseEntity<PlayerProfile> createPlayerProfile(@Valid @RequestBody PlayerProfile playerProfile) throws URISyntaxException {
        log.debug("REST request to save PlayerProfile : {}", playerProfile);
        if (playerProfile.getId() != null) {
            throw new BadRequestAlertException("A new playerProfile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PlayerProfile result = playerProfileRepository.save(playerProfile);
        return ResponseEntity
            .created(new URI("/api/player-profiles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /player-profiles/:id} : Updates an existing playerProfile.
     *
     * @param id the id of the playerProfile to save.
     * @param playerProfile the playerProfile to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated playerProfile,
     * or with status {@code 400 (Bad Request)} if the playerProfile is not valid,
     * or with status {@code 500 (Internal Server Error)} if the playerProfile couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/player-profiles/{id}")
    public ResponseEntity<PlayerProfile> updatePlayerProfile(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PlayerProfile playerProfile
    ) throws URISyntaxException {
        log.debug("REST request to update PlayerProfile : {}, {}", id, playerProfile);
        if (playerProfile.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, playerProfile.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!playerProfileRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PlayerProfile result = playerProfileRepository.save(playerProfile);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, playerProfile.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /player-profiles/:id} : Partial updates given fields of an existing playerProfile, field will ignore if it is null
     *
     * @param id the id of the playerProfile to save.
     * @param playerProfile the playerProfile to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated playerProfile,
     * or with status {@code 400 (Bad Request)} if the playerProfile is not valid,
     * or with status {@code 404 (Not Found)} if the playerProfile is not found,
     * or with status {@code 500 (Internal Server Error)} if the playerProfile couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/player-profiles/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PlayerProfile> partialUpdatePlayerProfile(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PlayerProfile playerProfile
    ) throws URISyntaxException {
        log.debug("REST request to partial update PlayerProfile partially : {}, {}", id, playerProfile);
        if (playerProfile.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, playerProfile.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!playerProfileRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PlayerProfile> result = playerProfileRepository
            .findById(playerProfile.getId())
            .map(existingPlayerProfile -> {
                if (playerProfile.getActiveCardIdx() != null) {
                    existingPlayerProfile.setActiveCardIdx(playerProfile.getActiveCardIdx());
                }

                return existingPlayerProfile;
            })
            .map(playerProfileRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, playerProfile.getId().toString())
        );
    }

    /**
     * {@code GET  /player-profiles} : get all the playerProfiles.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of playerProfiles in body.
     */
    @GetMapping("/player-profiles")
    public List<PlayerProfile> getAllPlayerProfiles(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all PlayerProfiles");
        if (eagerload) {
            return playerProfileRepository.findAllWithEagerRelationships();
        } else {
            return playerProfileRepository.findAll();
        }
    }

    /**
     * {@code GET  /player-profiles/:id} : get the "id" playerProfile.
     *
     * @param id the id of the playerProfile to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the playerProfile, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/player-profiles/{id}")
    public ResponseEntity<PlayerProfile> getPlayerProfile(@PathVariable Long id) {
        log.debug("REST request to get PlayerProfile : {}", id);
        Optional<PlayerProfile> playerProfile = playerProfileRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(playerProfile);
    }

    /**
     * {@code DELETE  /player-profiles/:id} : delete the "id" playerProfile.
     *
     * @param id the id of the playerProfile to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/player-profiles/{id}")
    public ResponseEntity<Void> deletePlayerProfile(@PathVariable Long id) {
        log.debug("REST request to delete PlayerProfile : {}", id);
        playerProfileRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
