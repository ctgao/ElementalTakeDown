import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './card-profile.reducer';

export const CardProfileDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const cardProfileEntity = useAppSelector(state => state.cardProfile.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="cardProfileDetailsHeading">Card Profile</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{cardProfileEntity.id}</dd>
          <dt>
            <span id="currentHP">Current HP</span>
          </dt>
          <dd>{cardProfileEntity.currentHP}</dd>
          <dt>
            <span id="infusion">Infusion</span>
          </dt>
          <dd>{cardProfileEntity.infusion ? 'true' : 'false'}</dd>
          <dt>
            <span id="elementalStatus">Elemental Status</span>
          </dt>
          <dd>{cardProfileEntity.elementalStatus}</dd>
          <dt>Character</dt>
          <dd>{cardProfileEntity.character ? cardProfileEntity.character.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/card-profile" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/card-profile/${cardProfileEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default CardProfileDetail;
