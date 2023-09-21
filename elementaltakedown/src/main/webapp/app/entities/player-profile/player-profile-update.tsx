import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUserProfile } from 'app/shared/model/user-profile.model';
import { getEntities as getUserProfiles } from 'app/entities/user-profile/user-profile.reducer';
import { ICardProfile } from 'app/shared/model/card-profile.model';
import { getEntities as getCardProfiles } from 'app/entities/card-profile/card-profile.reducer';
import { IPlayerProfile } from 'app/shared/model/player-profile.model';
import { getEntity, updateEntity, createEntity, reset } from './player-profile.reducer';

export const PlayerProfileUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const userProfiles = useAppSelector(state => state.userProfile.entities);
  const cardProfiles = useAppSelector(state => state.cardProfile.entities);
  const playerProfileEntity = useAppSelector(state => state.playerProfile.entity);
  const loading = useAppSelector(state => state.playerProfile.loading);
  const updating = useAppSelector(state => state.playerProfile.updating);
  const updateSuccess = useAppSelector(state => state.playerProfile.updateSuccess);

  const handleClose = () => {
    navigate('/player-profile');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUserProfiles({}));
    dispatch(getCardProfiles({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...playerProfileEntity,
      ...values,
      user: userProfiles.find(it => it.id.toString() === values.user.toString()),
      card1: cardProfiles.find(it => it.id.toString() === values.card1.toString()),
      card2: cardProfiles.find(it => it.id.toString() === values.card2.toString()),
      card3: cardProfiles.find(it => it.id.toString() === values.card3.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...playerProfileEntity,
          user: playerProfileEntity?.user?.id,
          card1: playerProfileEntity?.card1?.id,
          card2: playerProfileEntity?.card2?.id,
          card3: playerProfileEntity?.card3?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="elementaltakedownApp.playerProfile.home.createOrEditLabel" data-cy="PlayerProfileCreateUpdateHeading">
            Create or edit a Player Profile
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField name="id" required readOnly id="player-profile-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField
                label="Active Card Idx"
                id="player-profile-activeCardIdx"
                name="activeCardIdx"
                data-cy="activeCardIdx"
                type="text"
                validate={{
                  min: { value: 1, message: 'This field should be at least 1.' },
                  max: { value: 3, message: 'This field cannot be more than 3.' },
                  validate: v => isNumber(v) || 'This field should be a number.',
                }}
              />
              <ValidatedField id="player-profile-user" name="user" data-cy="user" label="User" type="select">
                <option value="" key="0" />
                {userProfiles
                  ? userProfiles.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="player-profile-card1" name="card1" data-cy="card1" label="Card 1" type="select">
                <option value="" key="0" />
                {cardProfiles
                  ? cardProfiles.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="player-profile-card2" name="card2" data-cy="card2" label="Card 2" type="select">
                <option value="" key="0" />
                {cardProfiles
                  ? cardProfiles.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="player-profile-card3" name="card3" data-cy="card3" label="Card 3" type="select">
                <option value="" key="0" />
                {cardProfiles
                  ? cardProfiles.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/player-profile" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default PlayerProfileUpdate;
