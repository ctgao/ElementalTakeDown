import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IDamage } from 'app/shared/model/damage.model';
import { DmgElementType } from 'app/shared/model/enumerations/dmg-element-type.model';
import { getEntity, updateEntity, createEntity, reset } from './damage.reducer';

export const DamageUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const damageEntity = useAppSelector(state => state.damage.entity);
  const loading = useAppSelector(state => state.damage.loading);
  const updating = useAppSelector(state => state.damage.updating);
  const updateSuccess = useAppSelector(state => state.damage.updateSuccess);
  const dmgElementTypeValues = Object.keys(DmgElementType);

  const handleClose = () => {
    navigate('/damage');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...damageEntity,
      ...values,
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
          dmgElement: 'WATER',
          splashElement: 'WATER',
          ...damageEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="elementaltakedownApp.damage.home.createOrEditLabel" data-cy="DamageCreateUpdateHeading">
            Create or edit a Damage
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="damage-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Name" id="damage-name" name="name" data-cy="name" type="text" disabled />
              <ValidatedField
                label="Dmg Value"
                id="damage-dmgValue"
                name="dmgValue"
                data-cy="dmgValue"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  max: { value: 20, message: 'This field cannot be more than 20.' },
                  validate: v => isNumber(v) || 'This field should be a number.',
                }}
              />
              <ValidatedField label="Dmg Element" id="damage-dmgElement" name="dmgElement" data-cy="dmgElement" type="select">
                {dmgElementTypeValues.map(dmgElementType => (
                  <option value={dmgElementType} key={dmgElementType}>
                    {dmgElementType}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label="Splash Dmg"
                id="damage-splashDmg"
                name="splashDmg"
                data-cy="splashDmg"
                type="text"
                validate={{
                  max: { value: 5, message: 'This field cannot be more than 5.' },
                  validate: v => isNumber(v) || 'This field should be a number.',
                }}
              />
              <ValidatedField label="Splash Element" id="damage-splashElement" name="splashElement" data-cy="splashElement" type="select">
                {dmgElementTypeValues.map(dmgElementType => (
                  <option value={dmgElementType} key={dmgElementType}>
                    {dmgElementType}
                  </option>
                ))}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/damage" replace color="info">
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

export default DamageUpdate;
