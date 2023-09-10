import { ElementType } from 'app/shared/model/enumerations/element-type.model';

export interface IArchiveCard {
  id?: number;
  name?: string;
  element?: ElementType;
  owned?: boolean;
}

export const defaultValue: Readonly<IArchiveCard> = {};
