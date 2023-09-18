import axios from 'axios';
import { createAsyncThunk, isFulfilled, isPending, isRejected } from '@reduxjs/toolkit';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { IQueryParams, createEntitySlice, EntityState, serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import { IArchiveCard, defaultValue } from 'app/shared/model/archive.model';
import { IUserProfile } from 'app/shared/model/user-profile.model';
import { ICharacterCard } from 'app/shared/model/character-card.model';

const initialState: EntityState<IArchiveCard> = {
  loading: false,
  errorMessage: null,
  entities: [],
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
};

const apiUrl = 'api/archive';

// Actions

export const getUserSpecificEntities = createAsyncThunk('archive/fetch_user_entity_list', async (login: string) => {
  const requestUrl = `${apiUrl}/${login}?cacheBuster=${new Date().getTime()}`;
  return axios.get<IArchiveCard[]>(requestUrl);
});

export const getEntities = createAsyncThunk('archive/fetch_entity_list', async ({ page, size, sort }: IQueryParams) => {
  const requestUrl = `${apiUrl}?cacheBuster=${new Date().getTime()}`;
  return axios.get<IArchiveCard[]>(requestUrl);
});

export const getEntity = createAsyncThunk(
  'archive/fetch_entity',
  async (id: string | number) => {
    const requestUrl = `api/character-cards/${id}`;
    return axios.get<ICharacterCard>(requestUrl);
  },
  { serializeError: serializeAxiosError }
);

export const getEntitiesWithOwnership = createAsyncThunk('archive/fetch_owner_entity_list', async (login: string) => {
  const requestUrl = `${apiUrl}/${login}/all`;
  return axios.get<IArchiveCard[]>(requestUrl);
});

export interface IArchiveUpdateParams {
  login: string;
  entities: IArchiveCard[];
};

export const updateEntityCardsOnly = createAsyncThunk(
  'archive/update_entity_cards_only',
  async ({login, entities}: IArchiveUpdateParams, thunkAPI) => {
    entities.forEach(cleanEntity);
    const result = await axios.put<IUserProfile>(`${apiUrl}/${login}`, entities);
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

// slice

export const ArchiveSlice = createEntitySlice({
  name: 'archive',
  initialState,
  extraReducers(builder) {
    builder
      .addCase(getEntity.fulfilled, (state, action) => {
        state.loading = false;
        state.entity = action.payload.data;
      })
      .addMatcher(isFulfilled(getEntities, getUserSpecificEntities, getEntitiesWithOwnership), (state, action) => {
        const { data } = action.payload;

        return {
          ...state,
          loading: false,
          entities: data,
        };
      })
      .addMatcher(isFulfilled(updateEntityCardsOnly), (state, action) => {
        state.updating = false;
        state.loading = false;
        state.updateSuccess = true;
//         state.entity = action.payload.data;
      })
      .addMatcher(isPending(getUserSpecificEntities, getEntities, getEntitiesWithOwnership, getEntity), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.loading = true;
      })
      .addMatcher(isPending(updateEntityCardsOnly), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.updating = true;
      });
  },
});

// Reducer
export default ArchiveSlice.reducer;
