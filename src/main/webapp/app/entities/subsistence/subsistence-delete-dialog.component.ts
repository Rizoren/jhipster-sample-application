import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISubsistence } from 'app/shared/model/subsistence.model';
import { SubsistenceService } from './subsistence.service';

@Component({
  templateUrl: './subsistence-delete-dialog.component.html'
})
export class SubsistenceDeleteDialogComponent {
  subsistence?: ISubsistence;

  constructor(
    protected subsistenceService: SubsistenceService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.subsistenceService.delete(id).subscribe(() => {
      this.eventManager.broadcast('subsistenceListModification');
      this.activeModal.close();
    });
  }
}
