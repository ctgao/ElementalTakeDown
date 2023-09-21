import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './player-profile.reducer';

export const PlayerProfileDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const playerProfileEntity = useAppSelector(state => state.playerProfile.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="playerProfileDetailsHeading">Player Profile</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{playerProfileEntity.id}</dd>
          <dt>
            <span id="activeCardIdx">Active Card Idx</span>
          </dt>
          <dd>{playerProfileEntity.activeCardIdx}</dd>
          <dt>User</dt>
          <dd>{playerProfileEntity.user ? playerProfileEntity.user.name : ''}</dd>
          <dt>Card 1</dt>
          <dd>{playerProfileEntity.card1 ? playerProfileEntity.card1.id : ''}</dd>
          <dt>Card 2</dt>
          <dd>{playerProfileEntity.card2 ? playerProfileEntity.card2.id : ''}</dd>
          <dt>Card 3</dt>
          <dd>{playerProfileEntity.card3 ? playerProfileEntity.card3.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/player-profile" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/player-profile/${playerProfileEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default PlayerProfileDetail;
