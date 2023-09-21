import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import CardProfile from './card-profile';
import CardProfileDetail from './card-profile-detail';
import CardProfileUpdate from './card-profile-update';
import CardProfileDeleteDialog from './card-profile-delete-dialog';

const CardProfileRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<CardProfile />} />
    <Route path="new" element={<CardProfileUpdate />} />
    <Route path=":id">
      <Route index element={<CardProfileDetail />} />
      <Route path="edit" element={<CardProfileUpdate />} />
      <Route path="delete" element={<CardProfileDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CardProfileRoutes;
