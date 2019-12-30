import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JhipsterSampleApplicationSharedModule } from 'app/shared/shared.module';
import { SubsistenceComponent } from './subsistence.component';
import { SubsistenceDetailComponent } from './subsistence-detail.component';
import { SubsistenceUpdateComponent } from './subsistence-update.component';
import { SubsistenceDeleteDialogComponent } from './subsistence-delete-dialog.component';
import { subsistenceRoute } from './subsistence.route';

@NgModule({
  imports: [JhipsterSampleApplicationSharedModule, RouterModule.forChild(subsistenceRoute)],
  declarations: [SubsistenceComponent, SubsistenceDetailComponent, SubsistenceUpdateComponent, SubsistenceDeleteDialogComponent],
  entryComponents: [SubsistenceDeleteDialogComponent]
})
export class JhipsterSampleApplicationSubsistenceModule {}
