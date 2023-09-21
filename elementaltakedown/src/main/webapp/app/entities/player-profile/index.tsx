import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import PlayerProfile from './player-profile';
import PlayerProfileDetail from './player-profile-detail';
import PlayerProfileUpdate from './player-profile-update';
import PlayerProfileDeleteDialog from './player-profile-delete-dialog';

const PlayerProfileRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<PlayerProfile />} />
    <Route path="new" element={<PlayerProfileUpdate />} />
    <Route path=":id">
      <Route index element={<PlayerProfileDetail />} />
      <Route path="edit" element={<PlayerProfileUpdate />} />
      <Route path="delete" element={<PlayerProfileDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PlayerProfileRoutes;
