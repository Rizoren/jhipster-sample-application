import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ISubsistence, Subsistence } from 'app/shared/model/subsistence.model';
import { SubsistenceService } from './subsistence.service';
import { SubsistenceComponent } from './subsistence.component';
import { SubsistenceDetailComponent } from './subsistence-detail.component';
import { SubsistenceUpdateComponent } from './subsistence-update.component';

@Injectable({ providedIn: 'root' })
export class SubsistenceResolve implements Resolve<ISubsistence> {
  constructor(private service: SubsistenceService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISubsistence> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((subsistence: HttpResponse<Subsistence>) => {
          if (subsistence.body) {
            return of(subsistence.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Subsistence());
  }
}

export const subsistenceRoute: Routes = [
  {
    path: '',
    component: SubsistenceComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'jhipsterSampleApplicationApp.subsistence.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: SubsistenceDetailComponent,
    resolve: {
      subsistence: SubsistenceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jhipsterSampleApplicationApp.subsistence.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: SubsistenceUpdateComponent,
    resolve: {
      subsistence: SubsistenceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jhipsterSampleApplicationApp.subsistence.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: SubsistenceUpdateComponent,
    resolve: {
      subsistence: SubsistenceResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jhipsterSampleApplicationApp.subsistence.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];
