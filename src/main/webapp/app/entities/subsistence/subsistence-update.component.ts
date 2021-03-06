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
import { IDocument } from 'app/shared/model/document.model';
import { DocumentService } from 'app/entities/document/document.service';
import { IRegion } from 'app/shared/model/region.model';
import { RegionService } from 'app/entities/region/region.service';

type SelectableEntity = IDocument | IRegion;

@Component({
  selector: 'jhi-subsistence-update',
  templateUrl: './subsistence-update.component.html'
})
export class SubsistenceUpdateComponent implements OnInit {
  isSaving = false;

  docs: IDocument[] = [];

  regions: IRegion[] = [];

  editForm = this.fb.group({
    id: [],
    yearSL: [null, [Validators.required, Validators.minLength(4), Validators.maxLength(10)]],
    quarterSL: [null, [Validators.required, Validators.min(1), Validators.max(4)]],
    dateAcceptSL: [null, [Validators.required]],
    valuePerCapitaSL: [null, [Validators.min(0)]],
    valueForCapableSL: [null, [Validators.min(0)]],
    valueForPensionersSL: [null, [Validators.min(0)]],
    valueForChildrenSL: [null, [Validators.min(0)]],
    doc: [],
    region: []
  });

  constructor(
    protected subsistenceService: SubsistenceService,
    protected documentService: DocumentService,
    protected regionService: RegionService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ subsistence }) => {
      this.updateForm(subsistence);

      this.documentService
        .query({ 'subsistenceId.specified': 'false' })
        .pipe(
          map((res: HttpResponse<IDocument[]>) => {
            return res.body ? res.body : [];
          })
        )
        .subscribe((resBody: IDocument[]) => {
          if (!subsistence.doc || !subsistence.doc.id) {
            this.docs = resBody;
          } else {
            this.documentService
              .find(subsistence.doc.id)
              .pipe(
                map((subRes: HttpResponse<IDocument>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IDocument[]) => {
                this.docs = concatRes;
              });
          }
        });

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
      doc: subsistence.doc,
      region: subsistence.region
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
      doc: this.editForm.get(['doc'])!.value,
      region: this.editForm.get(['region'])!.value
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

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
