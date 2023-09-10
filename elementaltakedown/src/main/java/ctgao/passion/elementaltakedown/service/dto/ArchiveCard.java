package ctgao.passion.elementaltakedown.service.dto;

import ctgao.passion.elementaltakedown.domain.BasicATK;
import ctgao.passion.elementaltakedown.domain.CharacterCard;
import ctgao.passion.elementaltakedown.domain.SkillATK;
import ctgao.passion.elementaltakedown.domain.UltimateATK;
import ctgao.passion.elementaltakedown.domain.enumeration.ElementType;

import java.io.Serializable;

/**
 * A ArchiveCard.
 */
public class ArchiveCard implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private ElementType element;

    private BasicATK basic;

    private SkillATK skill;

    private UltimateATK ultimate;

    private Boolean owned;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public ArchiveCard(CharacterCard card){
        this.id = card.getId();
        this.name = card.getName();
        this.element = card.getElement();
        this.basic = card.getBasic();
        this.skill = card.getSkill();
        this.ultimate = card.getUltimate();
        this.owned = false;
    }

    public Long getId() {
        return this.id;
    }

    public ArchiveCard id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public ArchiveCard name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ElementType getElement() {
        return this.element;
    }

    public ArchiveCard element(ElementType element) {
        this.setElement(element);
        return this;
    }

    public void setElement(ElementType element) {
        this.element = element;
    }

    public BasicATK getBasic() {
        return this.basic;
    }

    public void setBasic(BasicATK basicATK) {
        this.basic = basicATK;
    }

    public ArchiveCard basic(BasicATK basicATK) {
        this.setBasic(basicATK);
        return this;
    }

    public SkillATK getSkill() {
        return this.skill;
    }

    public void setSkill(SkillATK skillATK) {
        this.skill = skillATK;
    }

    public ArchiveCard skill(SkillATK skillATK) {
        this.setSkill(skillATK);
        return this;
    }

    public UltimateATK getUltimate() {
        return this.ultimate;
    }

    public void setUltimate(UltimateATK ultimateATK) {
        this.ultimate = ultimateATK;
    }

    public ArchiveCard ultimate(UltimateATK ultimateATK) {
        this.setUltimate(ultimateATK);
        return this;
    }

    public Boolean getOwned() {
        return owned;
    }

    public void setOwned(Boolean owned) {
        this.owned = owned;
    }

// jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArchiveCard)) {
            return false;
        }
        return id != null && id.equals(((ArchiveCard) o).id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ArchiveCard{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", element='" + getElement() + "'" +
            ", owned by user='" + getOwned() + "'" +
            "}";
    }
}
