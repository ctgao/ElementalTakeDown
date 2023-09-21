package ctgao.passion.elementaltakedown.web.rest;

import ctgao.passion.elementaltakedown.domain.GameTable;
import ctgao.passion.elementaltakedown.repository.GameTableRepository;
import ctgao.passion.elementaltakedown.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ctgao.passion.elementaltakedown.domain.GameTable}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class GameTableResource {

    private final Logger log = LoggerFactory.getLogger(GameTableResource.class);

    private static final String ENTITY_NAME = "gameTable";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GameTableRepository gameTableRepository;

    public GameTableResource(GameTableRepository gameTableRepository) {
        this.gameTableRepository = gameTableRepository;
    }

    /**
     * {@code POST  /game-tables} : Create a new gameTable.
     *
     * @param gameTable the gameTable to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new gameTable, or with status {@code 400 (Bad Request)} if the gameTable has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/game-tables")
    public ResponseEntity<GameTable> createGameTable(@RequestBody GameTable gameTable) throws URISyntaxException {
        log.debug("REST request to save GameTable : {}", gameTable);
        if (gameTable.getId() != null) {
            throw new BadRequestAlertException("A new gameTable cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GameTable result = gameTableRepository.save(gameTable);
        return ResponseEntity
            .created(new URI("/api/game-tables/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /game-tables/:id} : Updates an existing gameTable.
     *
     * @param id the id of the gameTable to save.
     * @param gameTable the gameTable to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gameTable,
     * or with status {@code 400 (Bad Request)} if the gameTable is not valid,
     * or with status {@code 500 (Internal Server Error)} if the gameTable couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/game-tables/{id}")
    public ResponseEntity<GameTable> updateGameTable(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody GameTable gameTable
    ) throws URISyntaxException {
        log.debug("REST request to update GameTable : {}, {}", id, gameTable);
        if (gameTable.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gameTable.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gameTableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        GameTable result = gameTableRepository.save(gameTable);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, gameTable.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /game-tables/:id} : Partial updates given fields of an existing gameTable, field will ignore if it is null
     *
     * @param id the id of the gameTable to save.
     * @param gameTable the gameTable to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gameTable,
     * or with status {@code 400 (Bad Request)} if the gameTable is not valid,
     * or with status {@code 404 (Not Found)} if the gameTable is not found,
     * or with status {@code 500 (Internal Server Error)} if the gameTable couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/game-tables/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<GameTable> partialUpdateGameTable(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody GameTable gameTable
    ) throws URISyntaxException {
        log.debug("REST request to partial update GameTable partially : {}, {}", id, gameTable);
        if (gameTable.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gameTable.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gameTableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GameTable> result = gameTableRepository
            .findById(gameTable.getId())
            .map(existingGameTable -> {
                if (gameTable.getp1turn() != null) {
                    existingGameTable.setp1turn(gameTable.getp1turn());
                }

                return existingGameTable;
            })
            .map(gameTableRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, gameTable.getId().toString())
        );
    }

    /**
     * {@code GET  /game-tables} : get all the gameTables.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of gameTables in body.
     */
    @GetMapping("/game-tables")
    public List<GameTable> getAllGameTables() {
        log.debug("REST request to get all GameTables");
        return gameTableRepository.findAll();
    }

    /**
     * {@code GET  /game-tables/:id} : get the "id" gameTable.
     *
     * @param id the id of the gameTable to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the gameTable, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/game-tables/{id}")
    public ResponseEntity<GameTable> getGameTable(@PathVariable Long id) {
        log.debug("REST request to get GameTable : {}", id);
        Optional<GameTable> gameTable = gameTableRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(gameTable);
    }

    /**
     * {@code DELETE  /game-tables/:id} : delete the "id" gameTable.
     *
     * @param id the id of the gameTable to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/game-tables/{id}")
    public ResponseEntity<Void> deleteGameTable(@PathVariable Long id) {
        log.debug("REST request to delete GameTable : {}", id);
        gameTableRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
