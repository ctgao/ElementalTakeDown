import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import CharacterArchive from './character-archive';
import ArchiveDetail from './archive-detail';
import UpdateUserArchive from './update-archive';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route index element={<CharacterArchive />} />
        <Route path="update" element={<UpdateUserArchive />} />
        <Route path=":id" element={<ArchiveDetail />} />
      </ErrorBoundaryRoutes>
    </div>
  );
};
