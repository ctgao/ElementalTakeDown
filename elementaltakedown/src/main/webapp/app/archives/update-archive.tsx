import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IArchiveCard } from 'app/shared/model/archive.model';
import { getEntitiesWithOwnership, updateEntityCardsOnly } from './archive.reducer';
import { Checkbox } from './checkbox';
import { cleanEntity } from 'app/shared/util/entity-utils';

export const UpdateUserArchive = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const login = useAppSelector(state => state.authentication.account.login);
  const characterCardList = useAppSelector(state => state.archive.entities);
  const loading = useAppSelector(state => state.archive.loading);
  const updating = useAppSelector(state => state.archive.updating);
  const updateSuccess = useAppSelector(state => state.archive.updateSuccess);

  const [data, setData] = useState(characterCardList.map((card, index) => card.owned));

  const handleClose = () => {
    navigate('/archive');
  };

  useEffect(() => {
    dispatch(getEntitiesWithOwnership(login));
  }, []);

  useEffect(() => {
    console.log("we updated!");
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  useEffect(() => {
    console.log(characterCardList);
    if(!loading && !updating){
      const initialVals = characterCardList.map((card, index) => card.owned);
      setData(initialVals);
    }
  }, [loading]);

  const saveEntity = (event) => {
    event.preventDefault();

    let entities = [];
    for(let i = 0; i < characterCardList.length; i++){
      entities[i] = JSON.parse(JSON.stringify(characterCardList[i]));
      entities[i].owned = data[i];
    }

    dispatch(updateEntityCardsOnly({login, entities}));
  };

  const handleChange = (item) => {
    let newData = new Array(...data);
    newData[item.index - 1] = item.checked;
    const newDataTWO = new Array(...newData);
    setData(newData);
  }

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
                      <Checkbox
                        obj={{index: characterCard.id, name: characterCard.name, checked: data[characterCard.id - 1]}}
                        onChange={item => handleChange(item)}
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
