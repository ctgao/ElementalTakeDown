import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICharacterCard } from 'app/shared/model/character-card.model';
import { getEntities, getUserSpecificEntities } from 'app/entities/character-card/character-card.reducer';

export const CharacterArchive = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const loggedInUser = useAppSelector(state => state.authentication.account.login);
  const characterCardList = useAppSelector(state => state.characterCard.entities);
  const loading = useAppSelector(state => state.characterCard.loading);

  useEffect(() => {
    if(loggedInUser === undefined) dispatch(getEntities({}));
    else dispatch(getUserSpecificEntities(loggedInUser));
  }, [loggedInUser]);

//   const handleSyncList = () => {
//     dispatch(getEntities({}));
//   };

  return (
    <div>
      <h2 id="character-card-heading" data-cy="CharacterCardHeading">
        Personal Character Archive
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" disabled={true}>
            <FontAwesomeIcon icon="sync" /> Button
          </Button>
        </div>
      </h2>
      <div className="table-responsive">
        {characterCardList && characterCardList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>Name</th>
                <th>Element</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {characterCardList.map((characterCard, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/character-card/${characterCard.id}`} color="link" size="sm">
                      {characterCard.name}
                    </Button>
                  </td>
                  <td>{characterCard.element}</td>
                  <td className="text-end">
                    <Button tag={Link} to={`/character-card/${characterCard.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                      <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                    </Button>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Character Cards found</div>
        )}
      </div>
    </div>
  );
};

export default CharacterArchive;
