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
        <h1 data-cy="characterCardDetailsHeading"
            className="text-secondary"
            style={{textShadow: '0px 0px 2px #2554b3'}}>
          <u>Character Card</u>
        </h1>
        <dl style={{padding: '10px'}}>
          <dt><h3>{characterCardEntity.name}</h3></dt>
          <dd style={{paddingLeft: '25px', paddingBottom: '10px'}}
              className={characterCardEntity.element}>
            <h4><strong style={{textShadow: '0px 0px 2px #2554b3'}}>
              Element: {characterCardEntity.element}
            </strong></h4>
          </dd>
          <dt><h5>
            {characterCardEntity.basic ? characterCardEntity.basic.name : ''}
          </h5></dt>
          <dd style={{paddingLeft: '25px', paddingBottom: '10px', color: 'white'}}>
            {characterCardEntity.basic ? characterCardEntity.basic.description : ''}
          </dd>
          <dt><h5>{characterCardEntity.skill ? characterCardEntity.skill.name : ''}</h5></dt>
          <dd style={{paddingLeft: '25px', paddingBottom: '10px', color: 'white'}}>
            {characterCardEntity.skill ? characterCardEntity.skill.description : ''}
          </dd>
          <dt><h5>{characterCardEntity.ultimate ? characterCardEntity.ultimate.name : ''}</h5></dt>
          <dd style={{paddingLeft: '25px', paddingBottom: '10px', color: 'white'}}>
            Required Energy: {characterCardEntity.ultimate ? characterCardEntity.ultimate.requiredEnergy : ''}
            <br />{characterCardEntity.ultimate ? characterCardEntity.ultimate.description : ''}
          </dd>
        </dl>
        <Button tag={Link} to="/archive" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
      </Col>
    </Row>
  );
};

export default ArchiveDetail;
