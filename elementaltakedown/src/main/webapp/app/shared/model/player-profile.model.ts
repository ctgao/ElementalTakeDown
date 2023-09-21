import { IUserProfile } from 'app/shared/model/user-profile.model';
import { ICardProfile } from 'app/shared/model/card-profile.model';

export interface IPlayerProfile {
  id?: number;
  activeCardIdx?: number | null;
  user?: IUserProfile | null;
  card1?: ICardProfile | null;
  card2?: ICardProfile | null;
  card3?: ICardProfile | null;
}

export const defaultValue: Readonly<IPlayerProfile> = {};
