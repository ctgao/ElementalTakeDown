import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './game-table.reducer';

export const GameTableDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const gameTableEntity = useAppSelector(state => state.gameTable.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="gameTableDetailsHeading">Game Table</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{gameTableEntity.id}</dd>
          <dt>
            <span id="p1turn">P 1 Turn</span>
          </dt>
          <dd>{gameTableEntity.p1turn ? 'true' : 'false'}</dd>
          <dt>Player One</dt>
          <dd>{gameTableEntity.playerOne ? gameTableEntity.playerOne.id : ''}</dd>
          <dt>Player Two</dt>
          <dd>{gameTableEntity.playerTwo ? gameTableEntity.playerTwo.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/game-table" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/game-table/${gameTableEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default GameTableDetail;
