import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { SubsistenceService } from 'app/entities/subsistence/subsistence.service';
import { ISubsistence, Subsistence } from 'app/shared/model/subsistence.model';

describe('Service Tests', () => {
  describe('Subsistence Service', () => {
    let injector: TestBed;
    let service: SubsistenceService;
    let httpMock: HttpTestingController;
    let elemDefault: ISubsistence;
    let expectedResult: ISubsistence | ISubsistence[] | boolean | null;
    let currentDate: moment.Moment;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(SubsistenceService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Subsistence(0, 'AAAAAAA', 0, currentDate, 0, 0, 0, 0);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            dateAcceptSL: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        service
          .find(123)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Subsistence', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dateAcceptSL: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            dateAcceptSL: currentDate
          },
          returnedFromService
        );
        service
          .create(new Subsistence())
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp.body));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Subsistence', () => {
        const returnedFromService = Object.assign(
          {
            yearSL: 'BBBBBB',
            quarterSL: 1,
            dateAcceptSL: currentDate.format(DATE_TIME_FORMAT),
            valuePerCapitaSL: 1,
            valueForCapableSL: 1,
            valueForPensionersSL: 1,
            valueForChildrenSL: 1
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateAcceptSL: currentDate
          },
          returnedFromService
        );
        service
          .update(expected)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp.body));
        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Subsistence', () => {
        const returnedFromService = Object.assign(
          {
            yearSL: 'BBBBBB',
            quarterSL: 1,
            dateAcceptSL: currentDate.format(DATE_TIME_FORMAT),
            valuePerCapitaSL: 1,
            valueForCapableSL: 1,
            valueForPensionersSL: 1,
            valueForChildrenSL: 1
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            dateAcceptSL: currentDate
          },
          returnedFromService
        );
        service
          .query()
          .pipe(
            take(1),
            map(resp => resp.body)
          )
          .subscribe(body => (expectedResult = body));
        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Subsistence', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
