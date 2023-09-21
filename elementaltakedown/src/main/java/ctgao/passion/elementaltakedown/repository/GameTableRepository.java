package ctgao.passion.elementaltakedown.repository;

import ctgao.passion.elementaltakedown.domain.GameTable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the GameTable entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GameTableRepository extends JpaRepository<GameTable, Long> {}
