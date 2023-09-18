import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faPenToSquare } from '@fortawesome/free-regular-svg-icons'

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IArchiveCard } from 'app/shared/model/archive.model';
import { getEntities, getUserSpecificEntities } from './archive.reducer';

export const CharacterArchive = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const loggedInUser = useAppSelector(state => state.authentication.account.login);
  const characterCardList = useAppSelector(state => state.archive.entities);
  const loading = useAppSelector(state => state.archive.loading);

  useEffect(() => {
    if(loggedInUser === undefined) dispatch(getEntities({}));
    else dispatch(getUserSpecificEntities(loggedInUser));
  }, []);

  return (
    <div>
      <h1 id="character-card-heading" data-cy="CharacterCardHeading">
        Personal Character Archive
        <div className="d-flex justify-content-end">
          {loggedInUser !== undefined ?
            <Button tag={Link} to={`/archive/update`} className="me-2" color="primary">
              <FontAwesomeIcon icon={faPenToSquare} /> Edit Archive
            </Button>
            : <span>&nbsp;</span>
          }
        </div>
      </h2>
      <div className="table-responsive">
        {characterCardList && characterCardList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th><h3 className="text-light">Name</h3></th>
                <th><h3 className="text-light">Element</h3></th>
                <th><h3 className="text-light">Basic</h3></th>
                <th><h3 className="text-light">Skill</h3></th>
                <th><h3 className="text-light">Ultimate</h3></th>
                <th />
              </tr>
            </thead>
            <tbody>
              {characterCardList.map((characterCard, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>{characterCard.name}</td>
                  <td>{characterCard.element}</td>
                  <td>{characterCard.basic ? characterCard.basic.name : ''}</td>
                  <td>{characterCard.skill ? characterCard.skill.name : ''}</td>
                  <td>{characterCard.ultimate ? characterCard.ultimate.name : ''}</td>
                  <td className="text-end">
                    <Button tag={Link} to={`/archive/${characterCard.id}`} color="info" size="sm" data-cy="entityDetailsButton">
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
