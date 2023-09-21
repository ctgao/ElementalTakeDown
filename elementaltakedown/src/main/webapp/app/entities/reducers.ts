import userProfile from 'app/entities/user-profile/user-profile.reducer';
import characterCard from 'app/entities/character-card/character-card.reducer';
import basicATK from 'app/entities/basic-atk/basic-atk.reducer';
import skillATK from 'app/entities/skill-atk/skill-atk.reducer';
import ultimateATK from 'app/entities/ultimate-atk/ultimate-atk.reducer';
import damage from 'app/entities/damage/damage.reducer';
import gameTable from 'app/entities/game-table/game-table.reducer';
import playerProfile from 'app/entities/player-profile/player-profile.reducer';
import cardProfile from 'app/entities/card-profile/card-profile.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  userProfile,
  characterCard,
  basicATK,
  skillATK,
  ultimateATK,
  damage,
  gameTable,
  playerProfile,
  cardProfile,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
