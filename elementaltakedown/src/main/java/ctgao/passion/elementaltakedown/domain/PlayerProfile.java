package ctgao.passion.elementaltakedown.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PlayerProfile.
 */
@Entity
@Table(name = "player_profile")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PlayerProfile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Min(value = 1)
    @Max(value = 3)
    @Column(name = "active_card_idx")
    private Integer activeCardIdx;

    @JsonIgnoreProperties(value = { "user", "cards" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private UserProfile user;

    @JsonIgnoreProperties(value = { "character" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private CardProfile card1;

    @JsonIgnoreProperties(value = { "character" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private CardProfile card2;

    @JsonIgnoreProperties(value = { "character" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private CardProfile card3;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PlayerProfile id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getActiveCardIdx() {
        return this.activeCardIdx;
    }

    public PlayerProfile activeCardIdx(Integer activeCardIdx) {
        this.setActiveCardIdx(activeCardIdx);
        return this;
    }

    public void setActiveCardIdx(Integer activeCardIdx) {
        this.activeCardIdx = activeCardIdx;
    }

    public UserProfile getUser() {
        return this.user;
    }

    public void setUser(UserProfile userProfile) {
        this.user = userProfile;
    }

    public PlayerProfile user(UserProfile userProfile) {
        this.setUser(userProfile);
        return this;
    }

    public CardProfile getCard1() {
        return this.card1;
    }

    public void setCard1(CardProfile cardProfile) {
        this.card1 = cardProfile;
    }

    public PlayerProfile card1(CardProfile cardProfile) {
        this.setCard1(cardProfile);
        return this;
    }

    public CardProfile getCard2() {
        return this.card2;
    }

    public void setCard2(CardProfile cardProfile) {
        this.card2 = cardProfile;
    }

    public PlayerProfile card2(CardProfile cardProfile) {
        this.setCard2(cardProfile);
        return this;
    }

    public CardProfile getCard3() {
        return this.card3;
    }

    public void setCard3(CardProfile cardProfile) {
        this.card3 = cardProfile;
    }

    public PlayerProfile card3(CardProfile cardProfile) {
        this.setCard3(cardProfile);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlayerProfile)) {
            return false;
        }
        return id != null && id.equals(((PlayerProfile) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PlayerProfile{" +
            "id=" + getId() +
            ", activeCardIdx=" + getActiveCardIdx() +
            "}";
    }
}
