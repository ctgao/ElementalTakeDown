package ctgao.passion.elementaltakedown.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ctgao.passion.elementaltakedown.domain.enumeration.DmgElementType;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CardProfile.
 */
@Entity
@Table(name = "card_profile")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CardProfile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Min(value = 0)
    @Max(value = 10)
    @Column(name = "current_hp", nullable = false)
    private Integer currentHP;

    @NotNull
    @Column(name = "infusion", nullable = false)
    private Boolean infusion;

    @Enumerated(EnumType.STRING)
    @Column(name = "elemental_status")
    private DmgElementType elementalStatus;

    @ManyToOne
    @JsonIgnoreProperties(value = { "basic", "skill", "ultimate", "owners" }, allowSetters = true)
    private CharacterCard character;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CardProfile id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCurrentHP() {
        return this.currentHP;
    }

    public CardProfile currentHP(Integer currentHP) {
        this.setCurrentHP(currentHP);
        return this;
    }

    public void setCurrentHP(Integer currentHP) {
        this.currentHP = currentHP;
    }

    public Boolean getInfusion() {
        return this.infusion;
    }

    public CardProfile infusion(Boolean infusion) {
        this.setInfusion(infusion);
        return this;
    }

    public void setInfusion(Boolean infusion) {
        this.infusion = infusion;
    }

    public DmgElementType getElementalStatus() {
        return this.elementalStatus;
    }

    public CardProfile elementalStatus(DmgElementType elementalStatus) {
        this.setElementalStatus(elementalStatus);
        return this;
    }

    public void setElementalStatus(DmgElementType elementalStatus) {
        this.elementalStatus = elementalStatus;
    }

    public CharacterCard getCharacter() {
        return this.character;
    }

    public void setCharacter(CharacterCard characterCard) {
        this.character = characterCard;
    }

    public CardProfile character(CharacterCard characterCard) {
        this.setCharacter(characterCard);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CardProfile)) {
            return false;
        }
        return id != null && id.equals(((CardProfile) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CardProfile{" +
            "id=" + getId() +
            ", currentHP=" + getCurrentHP() +
            ", infusion='" + getInfusion() + "'" +
            ", elementalStatus='" + getElementalStatus() + "'" +
            "}";
    }
}
