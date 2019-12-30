import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { ISubsistence, Subsistence } from 'app/shared/model/subsistence.model';
import { SubsistenceService } from './subsistence.service';
import { IRegion } from 'app/shared/model/region.model';
import { RegionService } from 'app/entities/region/region.service';

@Component({
  selector: 'jhi-subsistence-update',
  templateUrl: './subsistence-update.component.html'
})
export class SubsistenceUpdateComponent implements OnInit {
  isSaving = false;

  regions: IRegion[] = [];

  editForm = this.fb.group({
    id: [],
    yearSL: [],
    quarterSL: [],
    dateAcceptSL: [],
    valuePerCapitaSL: [],
    valueForCapableSL: [],
    valueForPensionersSL: [],
    valueForChildrenSL: [],
    subsistence: []
  });

  constructor(
    protected subsistenceService: SubsistenceService,
    protected regionService: RegionService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ subsistence }) => {
      this.updateForm(subsistence);

      this.regionService
        .query()
        .pipe(
          map((res: HttpResponse<IRegion[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IRegion[]) => (this.regions = resBody));
    });
  }

  updateForm(subsistence: ISubsistence): void {
    this.editForm.patchValue({
      id: subsistence.id,
      yearSL: subsistence.yearSL,
      quarterSL: subsistence.quarterSL,
      dateAcceptSL: subsistence.dateAcceptSL != null ? subsistence.dateAcceptSL.format(DATE_TIME_FORMAT) : null,
      valuePerCapitaSL: subsistence.valuePerCapitaSL,
      valueForCapableSL: subsistence.valueForCapableSL,
      valueForPensionersSL: subsistence.valueForPensionersSL,
      valueForChildrenSL: subsistence.valueForChildrenSL,
      subsistence: subsistence.subsistence
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const subsistence = this.createFromForm();
    if (subsistence.id !== undefined) {
      this.subscribeToSaveResponse(this.subsistenceService.update(subsistence));
    } else {
      this.subscribeToSaveResponse(this.subsistenceService.create(subsistence));
    }
  }

  private createFromForm(): ISubsistence {
    return {
      ...new Subsistence(),
      id: this.editForm.get(['id'])!.value,
      yearSL: this.editForm.get(['yearSL'])!.value,
      quarterSL: this.editForm.get(['quarterSL'])!.value,
      dateAcceptSL:
        this.editForm.get(['dateAcceptSL'])!.value != null
          ? moment(this.editForm.get(['dateAcceptSL'])!.value, DATE_TIME_FORMAT)
          : undefined,
      valuePerCapitaSL: this.editForm.get(['valuePerCapitaSL'])!.value,
      valueForCapableSL: this.editForm.get(['valueForCapableSL'])!.value,
      valueForPensionersSL: this.editForm.get(['valueForPensionersSL'])!.value,
      valueForChildrenSL: this.editForm.get(['valueForChildrenSL'])!.value,
      subsistence: this.editForm.get(['subsistence'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISubsistence>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: IRegion): any {
    return item.id;
  }
}
