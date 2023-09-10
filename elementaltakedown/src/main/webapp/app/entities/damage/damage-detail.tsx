import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './damage.reducer';

export const DamageDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const damageEntity = useAppSelector(state => state.damage.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="damageDetailsHeading">Damage</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{damageEntity.id}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{damageEntity.name}</dd>
          <dt>
            <span id="dmgValue">Dmg Value</span>
          </dt>
          <dd>{damageEntity.dmgValue}</dd>
          <dt>
            <span id="dmgElement">Dmg Element</span>
          </dt>
          <dd>{damageEntity.dmgElement}</dd>
          <dt>
            <span id="splashDmg">Splash Dmg</span>
          </dt>
          <dd>{damageEntity.splashDmg}</dd>
          <dt>
            <span id="splashElement">Splash Element</span>
          </dt>
          <dd>{damageEntity.splashElement}</dd>
        </dl>
        <Button tag={Link} to="/damage" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/damage/${damageEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default DamageDetail;
