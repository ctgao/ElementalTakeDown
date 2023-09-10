import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICharacterCard } from 'app/shared/model/character-card.model';
import { getEntities, getUserSpecificEntities } from 'app/entities/character-card/character-card.reducer';

export const UpdateUserArchive = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const loggedInUser = useAppSelector(state => state.authentication.account.login);
  const characterCardList = useAppSelector(state => state.characterCard.entities);
  const loading = useAppSelector(state => state.characterCard.loading);
  const ownedCards = useAppSelector(state => state.characterCard.userEntities);

  const updating = useAppSelector(state => state.userProfile.updating);
  const updateSuccess = useAppSelector(state => state.userProfile.updateSuccess);

  useEffect(() => {
    dispatch(getEntities({}));
    dispatch(getUserSpecificEntities(loggedInUser));
  }, []);

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
                  <td className="text-end">
                    Checkbox goes here
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Character Cards found</div>
        )}
      </div>
      <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/archive" replace color="info">
        <FontAwesomeIcon icon="arrow-left" />
        &nbsp;
        <span className="d-none d-md-inline">Back</span>
      </Button>
      &nbsp;
      <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
        <FontAwesomeIcon icon="save" />
        &nbsp; Save
      </Button>
    </div>
  );
};

export default UpdateUserArchive;
