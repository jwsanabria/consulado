import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Kanbam from './kanbam';
import KanbamDetail from './kanbam-detail';
import KanbamUpdate from './kanbam-update';
import KanbamDeleteDialog from './kanbam-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={KanbamUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={KanbamUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={KanbamDetail} />
      <ErrorBoundaryRoute path={match.url} component={Kanbam} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={KanbamDeleteDialog} />
  </>
);

export default Routes;
