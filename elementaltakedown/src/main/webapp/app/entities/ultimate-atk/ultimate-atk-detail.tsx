import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './ultimate-atk.reducer';

export const UltimateATKDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const ultimateATKEntity = useAppSelector(state => state.ultimateATK.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="ultimateATKDetailsHeading">Ultimate ATK</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{ultimateATKEntity.id}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{ultimateATKEntity.name}</dd>
          <dt>
            <span id="description">Description</span>
          </dt>
          <dd>{ultimateATKEntity.description}</dd>
          <dt>
            <span id="requiredEnergy">Required Energy</span>
          </dt>
          <dd>{ultimateATKEntity.requiredEnergy}</dd>
          <dt>Damage</dt>
          <dd>{ultimateATKEntity.damage ? ultimateATKEntity.damage.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/ultimate-atk" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/ultimate-atk/${ultimateATKEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default UltimateATKDetail;
