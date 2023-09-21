package ctgao.passion.elementaltakedown.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A GameTable.
 */
@Entity
@Table(name = "game_table")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GameTable implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "p_1_turn")
    private Boolean p1turn;

    @JsonIgnoreProperties(value = { "user", "card1", "card2", "card3" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private PlayerProfile playerOne;

    @JsonIgnoreProperties(value = { "user", "card1", "card2", "card3" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private PlayerProfile playerTwo;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public GameTable id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getp1turn() {
        return this.p1turn;
    }

    public GameTable p1turn(Boolean p1turn) {
        this.setp1turn(p1turn);
        return this;
    }

    public void setp1turn(Boolean p1turn) {
        this.p1turn = p1turn;
    }

    public PlayerProfile getPlayerOne() {
        return this.playerOne;
    }

    public void setPlayerOne(PlayerProfile playerProfile) {
        this.playerOne = playerProfile;
    }

    public GameTable playerOne(PlayerProfile playerProfile) {
        this.setPlayerOne(playerProfile);
        return this;
    }

    public PlayerProfile getPlayerTwo() {
        return this.playerTwo;
    }

    public void setPlayerTwo(PlayerProfile playerProfile) {
        this.playerTwo = playerProfile;
    }

    public GameTable playerTwo(PlayerProfile playerProfile) {
        this.setPlayerTwo(playerProfile);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GameTable)) {
            return false;
        }
        return id != null && id.equals(((GameTable) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GameTable{" +
            "id=" + getId() +
            ", p1turn='" + getp1turn() + "'" +
            "}";
    }
}
