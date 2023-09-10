import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './character-card.reducer';

export const CharacterCardDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const characterCardEntity = useAppSelector(state => state.characterCard.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="characterCardDetailsHeading">Character Card</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{characterCardEntity.id}</dd>
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
        &nbsp;
        <Button tag={Link} to={`/character-card/${characterCardEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default CharacterCardDetail;
