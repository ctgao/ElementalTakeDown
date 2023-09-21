import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICardProfile } from 'app/shared/model/card-profile.model';
import { getEntities } from './card-profile.reducer';

export const CardProfile = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const cardProfileList = useAppSelector(state => state.cardProfile.entities);
  const loading = useAppSelector(state => state.cardProfile.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="card-profile-heading" data-cy="CardProfileHeading">
        Card Profiles
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link to="/card-profile/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Card Profile
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {cardProfileList && cardProfileList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Current HP</th>
                <th>Infusion</th>
                <th>Elemental Status</th>
                <th>Character</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {cardProfileList.map((cardProfile, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/card-profile/${cardProfile.id}`} color="link" size="sm">
                      {cardProfile.id}
                    </Button>
                  </td>
                  <td>{cardProfile.currentHP}</td>
                  <td>{cardProfile.infusion ? 'true' : 'false'}</td>
                  <td>{cardProfile.elementalStatus}</td>
                  <td>
                    {cardProfile.character ? (
                      <Link to={`/character-card/${cardProfile.character.id}`}>{cardProfile.character.name}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/card-profile/${cardProfile.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`/card-profile/${cardProfile.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/card-profile/${cardProfile.id}/delete`}
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
          !loading && <div className="alert alert-warning">No Card Profiles found</div>
        )}
      </div>
    </div>
  );
};

export default CardProfile;
