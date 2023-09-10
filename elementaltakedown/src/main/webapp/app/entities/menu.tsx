import React from 'react';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/user-profile">
        User Profile
      </MenuItem>
      <MenuItem icon="asterisk" to="/character-card">
        Character Card
      </MenuItem>
      <MenuItem icon="asterisk" to="/basic-atk">
        Basic ATK
      </MenuItem>
      <MenuItem icon="asterisk" to="/skill-atk">
        Skill ATK
      </MenuItem>
      <MenuItem icon="asterisk" to="/ultimate-atk">
        Ultimate ATK
      </MenuItem>
      <MenuItem icon="asterisk" to="/damage">
        Damage
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
