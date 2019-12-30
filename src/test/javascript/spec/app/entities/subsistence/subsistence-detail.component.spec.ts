import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { JhipsterSampleApplicationTestModule } from '../../../test.module';
import { SubsistenceDetailComponent } from 'app/entities/subsistence/subsistence-detail.component';
import { Subsistence } from 'app/shared/model/subsistence.model';

describe('Component Tests', () => {
  describe('Subsistence Management Detail Component', () => {
    let comp: SubsistenceDetailComponent;
    let fixture: ComponentFixture<SubsistenceDetailComponent>;
    const route = ({ data: of({ subsistence: new Subsistence(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JhipsterSampleApplicationTestModule],
        declarations: [SubsistenceDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(SubsistenceDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SubsistenceDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load subsistence on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.subsistence).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
