import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IGameTable } from 'app/shared/model/game-table.model';
import { getEntities } from './game-table.reducer';

export const GameTable = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const gameTableList = useAppSelector(state => state.gameTable.entities);
  const loading = useAppSelector(state => state.gameTable.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="game-table-heading" data-cy="GameTableHeading">
        Game Tables
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link to="/game-table/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Game Table
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {gameTableList && gameTableList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>P 1 Turn</th>
                <th>Player One</th>
                <th>Player Two</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {gameTableList.map((gameTable, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/game-table/${gameTable.id}`} color="link" size="sm">
                      {gameTable.id}
                    </Button>
                  </td>
                  <td>{gameTable.p1turn ? 'true' : 'false'}</td>
                  <td>
                    {gameTable.playerOne ? <Link to={`/player-profile/${gameTable.playerOne.id}`}>{gameTable.playerOne.id}</Link> : ''}
                  </td>
                  <td>
                    {gameTable.playerTwo ? <Link to={`/player-profile/${gameTable.playerTwo.id}`}>{gameTable.playerTwo.id}</Link> : ''}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/game-table/${gameTable.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`/game-table/${gameTable.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`/game-table/${gameTable.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Game Tables found</div>
        )}
      </div>
    </div>
  );
};

export default GameTable;
