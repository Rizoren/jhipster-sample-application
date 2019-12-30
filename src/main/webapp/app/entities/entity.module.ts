import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'region',
        loadChildren: () => import('./region/region.module').then(m => m.JhipsterSampleApplicationRegionModule)
      },
      {
        path: 'subsistence',
        loadChildren: () => import('./subsistence/subsistence.module').then(m => m.JhipsterSampleApplicationSubsistenceModule)
      },
      {
        path: 'document',
        loadChildren: () => import('./document/document.module').then(m => m.JhipsterSampleApplicationDocumentModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class JhipsterSampleApplicationEntityModule {}
