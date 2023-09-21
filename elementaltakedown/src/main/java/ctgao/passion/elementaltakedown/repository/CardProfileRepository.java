package ctgao.passion.elementaltakedown.repository;

import ctgao.passion.elementaltakedown.domain.CardProfile;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CardProfile entity.
 */
@Repository
public interface CardProfileRepository extends JpaRepository<CardProfile, Long> {
    default Optional<CardProfile> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<CardProfile> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<CardProfile> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct cardProfile from CardProfile cardProfile left join fetch cardProfile.character",
        countQuery = "select count(distinct cardProfile) from CardProfile cardProfile"
    )
    Page<CardProfile> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct cardProfile from CardProfile cardProfile left join fetch cardProfile.character")
    List<CardProfile> findAllWithToOneRelationships();

    @Query("select cardProfile from CardProfile cardProfile left join fetch cardProfile.character where cardProfile.id =:id")
    Optional<CardProfile> findOneWithToOneRelationships(@Param("id") Long id);
}
