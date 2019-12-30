import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, SearchWithPagination } from 'app/shared/util/request-util';
import { ISubsistence } from 'app/shared/model/subsistence.model';

type EntityResponseType = HttpResponse<ISubsistence>;
type EntityArrayResponseType = HttpResponse<ISubsistence[]>;

@Injectable({ providedIn: 'root' })
export class SubsistenceService {
  public resourceUrl = SERVER_API_URL + 'api/subsistences';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/subsistences';

  constructor(protected http: HttpClient) {}

  create(subsistence: ISubsistence): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(subsistence);
    return this.http
      .post<ISubsistence>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(subsistence: ISubsistence): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(subsistence);
    return this.http
      .put<ISubsistence>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ISubsistence>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISubsistence[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISubsistence[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(subsistence: ISubsistence): ISubsistence {
    const copy: ISubsistence = Object.assign({}, subsistence, {
      dateAcceptSL: subsistence.dateAcceptSL && subsistence.dateAcceptSL.isValid() ? subsistence.dateAcceptSL.toJSON() : undefined
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateAcceptSL = res.body.dateAcceptSL ? moment(res.body.dateAcceptSL) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((subsistence: ISubsistence) => {
        subsistence.dateAcceptSL = subsistence.dateAcceptSL ? moment(subsistence.dateAcceptSL) : undefined;
      });
    }
    return res;
  }
}
