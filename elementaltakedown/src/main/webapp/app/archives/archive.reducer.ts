import axios from 'axios';
import { createAsyncThunk, isFulfilled, isPending, isRejected } from '@reduxjs/toolkit';

import { IQueryParams, createEntitySlice, EntityState, serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import { IArchiveCard, defaultValue } from 'app/shared/model/archive.model';

const initialState: EntityState<IArchiveCard> = {
  loading: false,
  errorMessage: null,
  entities: [],
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
};

const apiUrl = 'api/character-cards/archive/';

// Actions

export const getUserSpecificEntities = createAsyncThunk('archive/fetch_entity_list', async (login: string) => {
  const requestUrl = `${apiUrl}/${login}?cacheBuster=${new Date().getTime()}`;
  return axios.get<IArchiveCard[]>(requestUrl);
});

export const getEntities = createAsyncThunk('archive/fetch_entity_list', async ({ page, size, sort }: IQueryParams) => {
  const requestUrl = `${apiUrl}?cacheBuster=${new Date().getTime()}`;
  return axios.get<IArchiveCard[]>(requestUrl);
});

export interface IArchiveUpdateParams {
  login: string;
  sendingEntities: IArchiveCard[];
};

export const updateEntity = createAsyncThunk(
  'characterCard/update_entity',
  async ({login, sendingEntities}: IArchiveUpdateParams, thunkAPI) => {
    const result = await axios.put<IArchiveCard[]>(`${apiUrl}/${login}`, sendingEntities);
    thunkAPI.dispatch(getUserSpecificEntities(login));
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
      .addMatcher(isFulfilled(getUserSpecificEntities), (state, action) => {
        const { data } = action.payload;

        return {
          ...state,
          loading: false,
          entities: data,
        };
      })
      .addMatcher(isFulfilled(getEntities), (state, action) => {
        const { data } = action.payload;

        return {
          ...state,
          loading: false,
          entities: data,
        };
      })
      .addMatcher(isFulfilled(updateEntity), (state, action) => {
        state.updating = false;
        state.loading = false;
        state.updateSuccess = true;
        state.entities = action.payload.data;
      })
      .addMatcher(isPending(updateEntity), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.updating = true;
      });
  },
});

export const { reset } = ArchiveSlice.actions;

// Reducer
export default ArchiveSlice.reducer;
