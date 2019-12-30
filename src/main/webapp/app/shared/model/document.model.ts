import { Moment } from 'moment';
import { ISubsistence } from 'app/shared/model/subsistence.model';

export interface IDocument {
  id?: number;
  docName?: string;
  docDate?: Moment;
  docBlobContentType?: string;
  docBlob?: any;
  subsistence?: ISubsistence;
}

export class Document implements IDocument {
  constructor(
    public id?: number,
    public docName?: string,
    public docDate?: Moment,
    public docBlobContentType?: string,
    public docBlob?: any,
    public subsistence?: ISubsistence
  ) {}
}
