import { Moment } from 'moment';

export interface IDocument {
  id?: number;
  docName?: string;
  docDate?: Moment;
  docBlobContentType?: string;
  docBlob?: any;
}

export class Document implements IDocument {
  constructor(
    public id?: number,
    public docName?: string,
    public docDate?: Moment,
    public docBlobContentType?: string,
    public docBlob?: any
  ) {}
}
