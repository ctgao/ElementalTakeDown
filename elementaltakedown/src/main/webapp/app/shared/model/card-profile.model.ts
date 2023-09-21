import { ICharacterCard } from 'app/shared/model/character-card.model';
import { DmgElementType } from 'app/shared/model/enumerations/dmg-element-type.model';

export interface ICardProfile {
  id?: number;
  currentHP?: number;
  infusion?: boolean;
  elementalStatus?: DmgElementType | null;
  character?: ICharacterCard | null;
}

export const defaultValue: Readonly<ICardProfile> = {
  infusion: false,
};
