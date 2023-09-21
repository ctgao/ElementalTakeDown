import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPlayerProfile } from 'app/shared/model/player-profile.model';
import { getEntities as getPlayerProfiles } from 'app/entities/player-profile/player-profile.reducer';
import { IGameTable } from 'app/shared/model/game-table.model';
import { getEntity, updateEntity, createEntity, reset } from './game-table.reducer';

export const GameTableUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const playerProfiles = useAppSelector(state => state.playerProfile.entities);
  const gameTableEntity = useAppSelector(state => state.gameTable.entity);
  const loading = useAppSelector(state => state.gameTable.loading);
  const updating = useAppSelector(state => state.gameTable.updating);
  const updateSuccess = useAppSelector(state => state.gameTable.updateSuccess);

  const handleClose = () => {
    navigate('/game-table');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getPlayerProfiles({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...gameTableEntity,
      ...values,
      playerOne: playerProfiles.find(it => it.id.toString() === values.playerOne.toString()),
      playerTwo: playerProfiles.find(it => it.id.toString() === values.playerTwo.toString()),
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
          ...gameTableEntity,
          playerOne: gameTableEntity?.playerOne?.id,
          playerTwo: gameTableEntity?.playerTwo?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="elementaltakedownApp.gameTable.home.createOrEditLabel" data-cy="GameTableCreateUpdateHeading">
            Create or edit a Game Table
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="game-table-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="P 1 Turn" id="game-table-p1turn" name="p1turn" data-cy="p1turn" check type="checkbox" />
              <ValidatedField id="game-table-playerOne" name="playerOne" data-cy="playerOne" label="Player One" type="select">
                <option value="" key="0" />
                {playerProfiles
                  ? playerProfiles.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="game-table-playerTwo" name="playerTwo" data-cy="playerTwo" label="Player Two" type="select">
                <option value="" key="0" />
                {playerProfiles
                  ? playerProfiles.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/game-table" replace color="info">
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

export default GameTableUpdate;
