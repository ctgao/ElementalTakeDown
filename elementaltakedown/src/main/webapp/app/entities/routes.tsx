import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import UserProfile from './user-profile';
import CharacterCard from './character-card';
import BasicATK from './basic-atk';
import SkillATK from './skill-atk';
import UltimateATK from './ultimate-atk';
import Damage from './damage';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="user-profile/*" element={<UserProfile />} />
        <Route path="character-card/*" element={<CharacterCard />} />
        <Route path="basic-atk/*" element={<BasicATK />} />
        <Route path="skill-atk/*" element={<SkillATK />} />
        <Route path="ultimate-atk/*" element={<UltimateATK />} />
        <Route path="damage/*" element={<Damage />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};