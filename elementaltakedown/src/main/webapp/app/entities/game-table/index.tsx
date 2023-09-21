import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import GameTable from './game-table';
import GameTableDetail from './game-table-detail';
import GameTableUpdate from './game-table-update';
import GameTableDeleteDialog from './game-table-delete-dialog';

const GameTableRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<GameTable />} />
    <Route path="new" element={<GameTableUpdate />} />
    <Route path=":id">
      <Route index element={<GameTableDetail />} />
      <Route path="edit" element={<GameTableUpdate />} />
      <Route path="delete" element={<GameTableDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default GameTableRoutes;
