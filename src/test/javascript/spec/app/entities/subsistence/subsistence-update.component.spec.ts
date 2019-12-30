import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { JhipsterSampleApplicationTestModule } from '../../../test.module';
import { SubsistenceUpdateComponent } from 'app/entities/subsistence/subsistence-update.component';
import { SubsistenceService } from 'app/entities/subsistence/subsistence.service';
import { Subsistence } from 'app/shared/model/subsistence.model';

describe('Component Tests', () => {
  describe('Subsistence Management Update Component', () => {
    let comp: SubsistenceUpdateComponent;
    let fixture: ComponentFixture<SubsistenceUpdateComponent>;
    let service: SubsistenceService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JhipsterSampleApplicationTestModule],
        declarations: [SubsistenceUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(SubsistenceUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SubsistenceUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(SubsistenceService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Subsistence(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Subsistence();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
