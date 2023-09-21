package ctgao.passion.elementaltakedown.web.rest;

import ctgao.passion.elementaltakedown.domain.CardProfile;
import ctgao.passion.elementaltakedown.repository.CardProfileRepository;
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
 * REST controller for managing {@link ctgao.passion.elementaltakedown.domain.CardProfile}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CardProfileResource {

    private final Logger log = LoggerFactory.getLogger(CardProfileResource.class);

    private static final String ENTITY_NAME = "cardProfile";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CardProfileRepository cardProfileRepository;

    public CardProfileResource(CardProfileRepository cardProfileRepository) {
        this.cardProfileRepository = cardProfileRepository;
    }

    /**
     * {@code POST  /card-profiles} : Create a new cardProfile.
     *
     * @param cardProfile the cardProfile to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cardProfile, or with status {@code 400 (Bad Request)} if the cardProfile has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/card-profiles")
    public ResponseEntity<CardProfile> createCardProfile(@Valid @RequestBody CardProfile cardProfile) throws URISyntaxException {
        log.debug("REST request to save CardProfile : {}", cardProfile);
        if (cardProfile.getId() != null) {
            throw new BadRequestAlertException("A new cardProfile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CardProfile result = cardProfileRepository.save(cardProfile);
        return ResponseEntity
            .created(new URI("/api/card-profiles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /card-profiles/:id} : Updates an existing cardProfile.
     *
     * @param id the id of the cardProfile to save.
     * @param cardProfile the cardProfile to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cardProfile,
     * or with status {@code 400 (Bad Request)} if the cardProfile is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cardProfile couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/card-profiles/{id}")
    public ResponseEntity<CardProfile> updateCardProfile(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CardProfile cardProfile
    ) throws URISyntaxException {
        log.debug("REST request to update CardProfile : {}, {}", id, cardProfile);
        if (cardProfile.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cardProfile.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cardProfileRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CardProfile result = cardProfileRepository.save(cardProfile);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cardProfile.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /card-profiles/:id} : Partial updates given fields of an existing cardProfile, field will ignore if it is null
     *
     * @param id the id of the cardProfile to save.
     * @param cardProfile the cardProfile to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cardProfile,
     * or with status {@code 400 (Bad Request)} if the cardProfile is not valid,
     * or with status {@code 404 (Not Found)} if the cardProfile is not found,
     * or with status {@code 500 (Internal Server Error)} if the cardProfile couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/card-profiles/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CardProfile> partialUpdateCardProfile(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CardProfile cardProfile
    ) throws URISyntaxException {
        log.debug("REST request to partial update CardProfile partially : {}, {}", id, cardProfile);
        if (cardProfile.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cardProfile.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cardProfileRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CardProfile> result = cardProfileRepository
            .findById(cardProfile.getId())
            .map(existingCardProfile -> {
                if (cardProfile.getCurrentHP() != null) {
                    existingCardProfile.setCurrentHP(cardProfile.getCurrentHP());
                }
                if (cardProfile.getInfusion() != null) {
                    existingCardProfile.setInfusion(cardProfile.getInfusion());
                }
                if (cardProfile.getElementalStatus() != null) {
                    existingCardProfile.setElementalStatus(cardProfile.getElementalStatus());
                }

                return existingCardProfile;
            })
            .map(cardProfileRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cardProfile.getId().toString())
        );
    }

    /**
     * {@code GET  /card-profiles} : get all the cardProfiles.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cardProfiles in body.
     */
    @GetMapping("/card-profiles")
    public List<CardProfile> getAllCardProfiles(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all CardProfiles");
        if (eagerload) {
            return cardProfileRepository.findAllWithEagerRelationships();
        } else {
            return cardProfileRepository.findAll();
        }
    }

    /**
     * {@code GET  /card-profiles/:id} : get the "id" cardProfile.
     *
     * @param id the id of the cardProfile to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cardProfile, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/card-profiles/{id}")
    public ResponseEntity<CardProfile> getCardProfile(@PathVariable Long id) {
        log.debug("REST request to get CardProfile : {}", id);
        Optional<CardProfile> cardProfile = cardProfileRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(cardProfile);
    }

    /**
     * {@code DELETE  /card-profiles/:id} : delete the "id" cardProfile.
     *
     * @param id the id of the cardProfile to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/card-profiles/{id}")
    public ResponseEntity<Void> deleteCardProfile(@PathVariable Long id) {
        log.debug("REST request to delete CardProfile : {}", id);
        cardProfileRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
