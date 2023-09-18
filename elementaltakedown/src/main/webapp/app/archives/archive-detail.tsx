import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './archive.reducer';

export const ArchiveDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  const characterCardEntity = useAppSelector(state => state.archive.entity);

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  return (
    <Row>
      <Col md="8">
        <h1 data-cy="characterCardDetailsHeading">Character Card</h1>
        <dl className="jh-entity-details">
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{characterCardEntity.name}</dd>
          <dt>
            <span id="element">Element</span>
          </dt>
          <dd>{characterCardEntity.element}</dd>
          <dt>Basic</dt>
          <dd>{characterCardEntity.basic ? characterCardEntity.basic.name : ''}</dd>
          <dt>Skill</dt>
          <dd>{characterCardEntity.skill ? characterCardEntity.skill.name : ''}</dd>
          <dt>Ultimate</dt>
          <dd>{characterCardEntity.ultimate ? characterCardEntity.ultimate.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/character-card" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
      </Col>
    </Row>
  );
};

export default ArchiveDetail;
