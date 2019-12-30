import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISubsistence } from 'app/shared/model/subsistence.model';

@Component({
  selector: 'jhi-subsistence-detail',
  templateUrl: './subsistence-detail.component.html'
})
export class SubsistenceDetailComponent implements OnInit {
  subsistence: ISubsistence | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ subsistence }) => {
      this.subsistence = subsistence;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
