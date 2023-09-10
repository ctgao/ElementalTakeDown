import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUltimateATK } from 'app/shared/model/ultimate-atk.model';
import { getEntities } from './ultimate-atk.reducer';

export const UltimateATK = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const ultimateATKList = useAppSelector(state => state.ultimateATK.entities);
  const loading = useAppSelector(state => state.ultimateATK.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="ultimate-atk-heading" data-cy="UltimateATKHeading">
        Ultimate ATKS
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link to="/ultimate-atk/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Ultimate ATK
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {ultimateATKList && ultimateATKList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Description</th>
                <th>Required Energy</th>
                <th>Damage</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {ultimateATKList.map((ultimateATK, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/ultimate-atk/${ultimateATK.id}`} color="link" size="sm">
                      {ultimateATK.id}
                    </Button>
                  </td>
                  <td>{ultimateATK.name}</td>
                  <td>{ultimateATK.description}</td>
                  <td>{ultimateATK.requiredEnergy}</td>
                  <td>{ultimateATK.damage ? <Link to={`/damage/${ultimateATK.damage.id}`}>{ultimateATK.damage.name}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/ultimate-atk/${ultimateATK.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`/ultimate-atk/${ultimateATK.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/ultimate-atk/${ultimateATK.id}/delete`}
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
          !loading && <div className="alert alert-warning">No Ultimate ATKS found</div>
        )}
      </div>
    </div>
  );
};

export default UltimateATK;
