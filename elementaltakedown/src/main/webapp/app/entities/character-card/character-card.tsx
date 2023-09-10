import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICharacterCard } from 'app/shared/model/character-card.model';
import { getEntities } from './character-card.reducer';

export const CharacterCard = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const characterCardList = useAppSelector(state => state.characterCard.entities);
  const loading = useAppSelector(state => state.characterCard.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="character-card-heading" data-cy="CharacterCardHeading">
        Character Cards
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link to="/character-card/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Character Card
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {characterCardList && characterCardList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Element</th>
                <th>Basic</th>
                <th>Skill</th>
                <th>Ultimate</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {characterCardList.map((characterCard, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/character-card/${characterCard.id}`} color="link" size="sm">
                      {characterCard.id}
                    </Button>
                  </td>
                  <td>{characterCard.name}</td>
                  <td>{characterCard.element}</td>
                  <td>{characterCard.basic ? <Link to={`/basic-atk/${characterCard.basic.id}`}>{characterCard.basic.name}</Link> : ''}</td>
                  <td>{characterCard.skill ? <Link to={`/skill-atk/${characterCard.skill.id}`}>{characterCard.skill.name}</Link> : ''}</td>
                  <td>
                    {characterCard.ultimate ? (
                      <Link to={`/ultimate-atk/${characterCard.ultimate.id}`}>{characterCard.ultimate.name}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/character-card/${characterCard.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/character-card/${characterCard.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/character-card/${characterCard.id}/delete`}
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
          !loading && <div className="alert alert-warning">No Character Cards found</div>
        )}
      </div>
    </div>
  );
};

export default CharacterCard;
