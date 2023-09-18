package ctgao.passion.elementaltakedown.web.rest;

import ctgao.passion.elementaltakedown.config.Constants;
import ctgao.passion.elementaltakedown.domain.UserProfile;
import ctgao.passion.elementaltakedown.service.ArchiveService;
import ctgao.passion.elementaltakedown.service.dto.ArchiveCard;
import ctgao.passion.elementaltakedown.service.dto.UserProfileDTO;
import ctgao.passion.elementaltakedown.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;

import javax.validation.constraints.Pattern;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * REST controller for managing the Archive Cards.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ArchiveResource {

    private final Logger log = LoggerFactory.getLogger(UserProfileResource.class);

    private static final String ENTITY_NAME = "userArchives";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private ArchiveService archiveService;

    public ArchiveResource(ArchiveService archiveService) {
        this.archiveService = archiveService;
    }

    /**
     * {@code GET /archive : get all the cards as archive cards
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with list of cards, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/archive")
    public List<ArchiveCard> getAllCards() {
        log.debug("REST request to get all Archive Cards");
        return archiveService.getAllCards();
    }

    /**
     * {@code GET /archive/:login} : get the "logged in" user, get the associate "user profile", then return the list of cards
     * @param login the login of the User to retrieve their UserProfile.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with list of cards, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/archive/{login}")
    public List<ArchiveCard> getCardsOwned(@PathVariable @Pattern(regexp = Constants.LOGIN_REGEX) String login) {
        log.debug("REST request to get Cards of User : {}", login);
        // consequences: an empty profile gets created if that login doesn't have a profile already associated with it
        UserProfileDTO profileDTO = archiveService.findProfileByLogin(login);
        if(profileDTO != null) {
            return archiveService.getUserOwnedCards(profileDTO);
        }
        // should we even actually get here?
        return new ArrayList<>();
    }

    /**
     * {@code GET /archive/:login/all} : get the "logged in" user, get the associate "user profile", then return the list of cards
     * @param login the login of the User to retrieve their UserProfile.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with list of cards, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/archive/{login}/all")
    public List<ArchiveCard> getAllCardsIncludingOwnership(@PathVariable @Pattern(regexp = Constants.LOGIN_REGEX) String login) {
        log.debug("REST request to get all Cards with flags of ownership for User : {}", login);
        UserProfileDTO profileDTO = archiveService.findProfileByLogin(login);
        if(profileDTO != null) {
            List<ArchiveCard> finalCards = archiveService.getAllCardsWithOwnership(profileDTO);
            log.debug("I HAVE MADE IT HERE!");
            return finalCards;
        }
        // should we even actually get here?
        return new ArrayList<>();
    }

    /**
     * {@code PUT /archive/:login} : get the "logged in" user, update the associated "user profile"
     *
     * @param login the login of the User to retrieve their userProfile.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the list of cards, or with status {@code 404 (Not Found)}.
     */
    @PutMapping("/archive/{login}")
    public ResponseEntity<UserProfile> updateCardsOwned(
        @PathVariable @Pattern(regexp = Constants.LOGIN_REGEX) String login,
        @RequestBody List<ArchiveCard> archiveCards
    ) throws URISyntaxException {
        log.debug("REST request to get User : {}", login);
        UserProfileDTO profileDTO = archiveService.findProfileByLogin(login);
        if(profileDTO == null) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        UserProfile finalProfile = archiveService.updateProfile(profileDTO, archiveCards);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, finalProfile.getId().toString()))
            .body(finalProfile);
    }
}
