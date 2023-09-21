import { IPlayerProfile } from 'app/shared/model/player-profile.model';

export interface IGameTable {
  id?: number;
  p1turn?: boolean | null;
  playerOne?: IPlayerProfile | null;
  playerTwo?: IPlayerProfile | null;
}

export const defaultValue: Readonly<IGameTable> = {
  p1turn: false,
};
