package ctgao.passion.elementaltakedown.repository;

import ctgao.passion.elementaltakedown.domain.PlayerProfile;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PlayerProfile entity.
 */
@Repository
public interface PlayerProfileRepository extends JpaRepository<PlayerProfile, Long> {
    default Optional<PlayerProfile> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<PlayerProfile> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<PlayerProfile> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct playerProfile from PlayerProfile playerProfile left join fetch playerProfile.user",
        countQuery = "select count(distinct playerProfile) from PlayerProfile playerProfile"
    )
    Page<PlayerProfile> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct playerProfile from PlayerProfile playerProfile left join fetch playerProfile.user")
    List<PlayerProfile> findAllWithToOneRelationships();

    @Query("select playerProfile from PlayerProfile playerProfile left join fetch playerProfile.user where playerProfile.id =:id")
    Optional<PlayerProfile> findOneWithToOneRelationships(@Param("id") Long id);
}
