import { IBasicATK } from 'app/shared/model/basic-atk.model';
import { ISkillATK } from 'app/shared/model/skill-atk.model';
import { IUltimateATK } from 'app/shared/model/ultimate-atk.model';
import { ElementType } from 'app/shared/model/enumerations/element-type.model';

export interface IArchiveCard {
  id?: number;
  name?: string;
  element?: ElementType;
  basic?: IBasicATK;
  skill?: ISkillATK;
  ultimate?: IUltimateATK;
  owned?: boolean;
}

export const defaultValue: Readonly<IArchiveCard> = {};
