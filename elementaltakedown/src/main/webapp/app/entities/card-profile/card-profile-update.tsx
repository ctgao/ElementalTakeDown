import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICharacterCard } from 'app/shared/model/character-card.model';
import { getEntities as getCharacterCards } from 'app/entities/character-card/character-card.reducer';
import { ICardProfile } from 'app/shared/model/card-profile.model';
import { DmgElementType } from 'app/shared/model/enumerations/dmg-element-type.model';
import { getEntity, updateEntity, createEntity, reset } from './card-profile.reducer';

export const CardProfileUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const characterCards = useAppSelector(state => state.characterCard.entities);
  const cardProfileEntity = useAppSelector(state => state.cardProfile.entity);
  const loading = useAppSelector(state => state.cardProfile.loading);
  const updating = useAppSelector(state => state.cardProfile.updating);
  const updateSuccess = useAppSelector(state => state.cardProfile.updateSuccess);
  const dmgElementTypeValues = Object.keys(DmgElementType);

  const handleClose = () => {
    navigate('/card-profile');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getCharacterCards({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...cardProfileEntity,
      ...values,
      character: characterCards.find(it => it.id.toString() === values.character.toString()),
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
          elementalStatus: 'WATER',
          ...cardProfileEntity,
          character: cardProfileEntity?.character?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="elementaltakedownApp.cardProfile.home.createOrEditLabel" data-cy="CardProfileCreateUpdateHeading">
            Create or edit a Card Profile
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="card-profile-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Current HP"
                id="card-profile-currentHP"
                name="currentHP"
                data-cy="currentHP"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  min: { value: 0, message: 'This field should be at least 0.' },
                  max: { value: 10, message: 'This field cannot be more than 10.' },
                  validate: v => isNumber(v) || 'This field should be a number.',
                }}
              />
              <ValidatedField label="Infusion" id="card-profile-infusion" name="infusion" data-cy="infusion" check type="checkbox" />
              <ValidatedField
                label="Elemental Status"
                id="card-profile-elementalStatus"
                name="elementalStatus"
                data-cy="elementalStatus"
                type="select"
              >
                {dmgElementTypeValues.map(dmgElementType => (
                  <option value={dmgElementType} key={dmgElementType}>
                    {dmgElementType}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField id="card-profile-character" name="character" data-cy="character" label="Character" type="select">
                <option value="" key="0" />
                {characterCards
                  ? characterCards.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/card-profile" replace color="info">
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

export default CardProfileUpdate;
