import { Moment } from 'moment';
import { IRegion } from 'app/shared/model/region.model';

export interface ISubsistence {
  id?: number;
  yearSL?: number;
  quarterSL?: number;
  dateAcceptSL?: Moment;
  valuePerCapitaSL?: number;
  valueForCapableSL?: number;
  valueForPensionersSL?: number;
  valueForChildrenSL?: number;
  subsistence?: IRegion;
}

export class Subsistence implements ISubsistence {
  constructor(
    public id?: number,
    public yearSL?: number,
    public quarterSL?: number,
    public dateAcceptSL?: Moment,
    public valuePerCapitaSL?: number,
    public valueForCapableSL?: number,
    public valueForPensionersSL?: number,
    public valueForChildrenSL?: number,
    public subsistence?: IRegion
  ) {}
}
