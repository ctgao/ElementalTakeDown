import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IArchiveCard } from 'app/shared/model/archive.model';
import { getEntitiesWithOwnership, updateEntity } from './archive.reducer';

export const UpdateUserArchive = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const login = useAppSelector(state => state.authentication.account.login);
  const characterCardList = useAppSelector(state => state.characterCard.entities);
  const loading = useAppSelector(state => state.characterCard.loading);
  const updating = useAppSelector(state => state.characterCard.updating);
  const updateSuccess = useAppSelector(state => state.characterCard.updateSuccess);

  const [checkedState, setCheckedState] = useState(new Array(characterCardList.length).fill(false));

  useEffect(() => {
    dispatch(getEntitiesWithOwnership(login));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      navigate('/archive');
    }
  }, [updateSuccess]);

  useEffect(() => {
    const initialVals = characterCardList.map((card, index) => loading ? false : card.owned);
    setCheckedState(initialVals);
  }, [loading]);

  const saveEntity = () => {
    const entities = [];
    for(let i = 0; i < characterCardList.length; i++){
      entities[i] = characterCardList[i];
      entities[i].owned = checkedState[i];
    }
//     console.log(entities);
    dispatch(updateEntity({login, entities}));
  };

  const handleOnChange = (position) => {
    const updatedCheckedState = checkedState.map((item, index) =>
      index === position ? !item : item
    );
    setCheckedState(updatedCheckedState);
  };

  return (
    <div>
      <h1 id="character-card-heading" data-cy="CharacterCardHeading">
        Add Characters to Archive
      </h1>
      <form onSubmit={saveEntity}>
        <div className="table-responsive">
          {characterCardList && characterCardList.length > 0 ? (
            <Table responsive>
              <thead>
                <tr>
                  <th><h3 className="text-light">Name</h3></th>
                  <th><h3 className="text-light">Element</h3></th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {characterCardList.map((characterCard, i) => (
                  <tr key={`entity-${i}`} data-cy="entityTable">
                    <td>{characterCard.name}</td>
                    <td>{characterCard.element}</td>
                    <td className="text-end">
                      <input
                        type="checkbox"
                        id={`entity-${i}`}
                        checked={checkedState[i]}
                        onChange={() => handleOnChange(i)}
                      />
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
          ) : (
            !loading && <div className="alert alert-warning">No Character Cards found</div>
          )}
        </div>
        <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/archive" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />
          &nbsp;<span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;&nbsp;
        <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
          <FontAwesomeIcon icon="save" />
          &nbsp; Save
        </Button>
      </form>
    </div>
  );
};

export default UpdateUserArchive;
