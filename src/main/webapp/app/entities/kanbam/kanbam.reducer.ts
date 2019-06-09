import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IKanbam, defaultValue } from 'app/shared/model/kanbam.model';

export const ACTION_TYPES = {
  FETCH_KANBAM_LIST: 'kanbam/FETCH_KANBAM_LIST',
  FETCH_KANBAM: 'kanbam/FETCH_KANBAM',
  CREATE_KANBAM: 'kanbam/CREATE_KANBAM',
  UPDATE_KANBAM: 'kanbam/UPDATE_KANBAM',
  DELETE_KANBAM: 'kanbam/DELETE_KANBAM',
  RESET: 'kanbam/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IKanbam>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type KanbamState = Readonly<typeof initialState>;

// Reducer

export default (state: KanbamState = initialState, action): KanbamState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_KANBAM_LIST):
    case REQUEST(ACTION_TYPES.FETCH_KANBAM):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_KANBAM):
    case REQUEST(ACTION_TYPES.UPDATE_KANBAM):
    case REQUEST(ACTION_TYPES.DELETE_KANBAM):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_KANBAM_LIST):
    case FAILURE(ACTION_TYPES.FETCH_KANBAM):
    case FAILURE(ACTION_TYPES.CREATE_KANBAM):
    case FAILURE(ACTION_TYPES.UPDATE_KANBAM):
    case FAILURE(ACTION_TYPES.DELETE_KANBAM):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_KANBAM_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_KANBAM):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_KANBAM):
    case SUCCESS(ACTION_TYPES.UPDATE_KANBAM):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_KANBAM):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/kanbams';

// Actions

export const getEntities: ICrudGetAllAction<IKanbam> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_KANBAM_LIST,
  payload: axios.get<IKanbam>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IKanbam> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_KANBAM,
    payload: axios.get<IKanbam>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IKanbam> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_KANBAM,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IKanbam> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_KANBAM,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IKanbam> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_KANBAM,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
