import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPlayerProfile } from 'app/shared/model/player-profile.model';
import { getEntities } from './player-profile.reducer';

export const PlayerProfile = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const playerProfileList = useAppSelector(state => state.playerProfile.entities);
  const loading = useAppSelector(state => state.playerProfile.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="player-profile-heading" data-cy="PlayerProfileHeading">
        Player Profiles
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link to="/player-profile/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Player Profile
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {playerProfileList && playerProfileList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Active Card Idx</th>
                <th>User</th>
                <th>Card 1</th>
                <th>Card 2</th>
                <th>Card 3</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {playerProfileList.map((playerProfile, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/player-profile/${playerProfile.id}`} color="link" size="sm">
                      {playerProfile.id}
                    </Button>
                  </td>
                  <td>{playerProfile.activeCardIdx}</td>
                  <td>{playerProfile.user ? <Link to={`/user-profile/${playerProfile.user.id}`}>{playerProfile.user.name}</Link> : ''}</td>
                  <td>{playerProfile.card1 ? <Link to={`/card-profile/${playerProfile.card1.id}`}>{playerProfile.card1.id}</Link> : ''}</td>
                  <td>{playerProfile.card2 ? <Link to={`/card-profile/${playerProfile.card2.id}`}>{playerProfile.card2.id}</Link> : ''}</td>
                  <td>{playerProfile.card3 ? <Link to={`/card-profile/${playerProfile.card3.id}`}>{playerProfile.card3.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/player-profile/${playerProfile.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/player-profile/${playerProfile.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/player-profile/${playerProfile.id}/delete`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Player Profiles found</div>
        )}
      </div>
    </div>
  );
};

export default PlayerProfile;
