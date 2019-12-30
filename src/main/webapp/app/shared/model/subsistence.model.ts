import { Moment } from 'moment';
import { IDocument } from 'app/shared/model/document.model';
import { IRegion } from 'app/shared/model/region.model';

export interface ISubsistence {
  id?: number;
  yearSL?: string;
  quarterSL?: number;
  dateAcceptSL?: Moment;
  valuePerCapitaSL?: number;
  valueForCapableSL?: number;
  valueForPensionersSL?: number;
  valueForChildrenSL?: number;
  doc?: IDocument;
  region?: IRegion;
}

export class Subsistence implements ISubsistence {
  constructor(
    public id?: number,
    public yearSL?: string,
    public quarterSL?: number,
    public dateAcceptSL?: Moment,
    public valuePerCapitaSL?: number,
    public valueForCapableSL?: number,
    public valueForPensionersSL?: number,
    public valueForChildrenSL?: number,
    public doc?: IDocument,
    public region?: IRegion
  ) {}
}
