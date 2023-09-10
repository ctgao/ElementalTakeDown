import userProfile from 'app/entities/user-profile/user-profile.reducer';
import characterCard from 'app/entities/character-card/character-card.reducer';
import basicATK from 'app/entities/basic-atk/basic-atk.reducer';
import skillATK from 'app/entities/skill-atk/skill-atk.reducer';
import ultimateATK from 'app/entities/ultimate-atk/ultimate-atk.reducer';
import damage from 'app/entities/damage/damage.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  userProfile,
  characterCard,
  basicATK,
  skillATK,
  ultimateATK,
  damage,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
