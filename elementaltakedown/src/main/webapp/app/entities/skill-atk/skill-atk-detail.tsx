import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './skill-atk.reducer';

export const SkillATKDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const skillATKEntity = useAppSelector(state => state.skillATK.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="skillATKDetailsHeading">Skill ATK</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{skillATKEntity.id}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{skillATKEntity.name}</dd>
          <dt>
            <span id="description">Description</span>
          </dt>
          <dd>{skillATKEntity.description}</dd>
          <dt>Damage</dt>
          <dd>{skillATKEntity.damage ? skillATKEntity.damage.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/skill-atk" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/skill-atk/${skillATKEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default SkillATKDetail;
