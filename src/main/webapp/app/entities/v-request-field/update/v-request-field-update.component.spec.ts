import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { VRequestFieldFormService } from './v-request-field-form.service';
import { VRequestFieldService } from '../service/v-request-field.service';
import { IVRequestField } from '../v-request-field.model';
import { IVEndpoint } from 'app/entities/v-endpoint/v-endpoint.model';
import { VEndpointService } from 'app/entities/v-endpoint/service/v-endpoint.service';

import { VRequestFieldUpdateComponent } from './v-request-field-update.component';

describe('VRequestField Management Update Component', () => {
  let comp: VRequestFieldUpdateComponent;
  let fixture: ComponentFixture<VRequestFieldUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let vRequestFieldFormService: VRequestFieldFormService;
  let vRequestFieldService: VRequestFieldService;
  let vEndpointService: VEndpointService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [VRequestFieldUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(VRequestFieldUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(VRequestFieldUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    vRequestFieldFormService = TestBed.inject(VRequestFieldFormService);
    vRequestFieldService = TestBed.inject(VRequestFieldService);
    vEndpointService = TestBed.inject(VEndpointService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call VEndpoint query and add missing value', () => {
      const vRequestField: IVRequestField = { id: 456 };
      const vEndpoint: IVEndpoint = { id: 32271 };
      vRequestField.vEndpoint = vEndpoint;

      const vEndpointCollection: IVEndpoint[] = [{ id: 85295 }];
      jest.spyOn(vEndpointService, 'query').mockReturnValue(of(new HttpResponse({ body: vEndpointCollection })));
      const additionalVEndpoints = [vEndpoint];
      const expectedCollection: IVEndpoint[] = [...additionalVEndpoints, ...vEndpointCollection];
      jest.spyOn(vEndpointService, 'addVEndpointToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ vRequestField });
      comp.ngOnInit();

      expect(vEndpointService.query).toHaveBeenCalled();
      expect(vEndpointService.addVEndpointToCollectionIfMissing).toHaveBeenCalledWith(
        vEndpointCollection,
        ...additionalVEndpoints.map(expect.objectContaining)
      );
      expect(comp.vEndpointsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const vRequestField: IVRequestField = { id: 456 };
      const vEndpoint: IVEndpoint = { id: 889 };
      vRequestField.vEndpoint = vEndpoint;

      activatedRoute.data = of({ vRequestField });
      comp.ngOnInit();

      expect(comp.vEndpointsSharedCollection).toContain(vEndpoint);
      expect(comp.vRequestField).toEqual(vRequestField);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVRequestField>>();
      const vRequestField = { id: 123 };
      jest.spyOn(vRequestFieldFormService, 'getVRequestField').mockReturnValue(vRequestField);
      jest.spyOn(vRequestFieldService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vRequestField });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vRequestField }));
      saveSubject.complete();

      // THEN
      expect(vRequestFieldFormService.getVRequestField).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(vRequestFieldService.update).toHaveBeenCalledWith(expect.objectContaining(vRequestField));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVRequestField>>();
      const vRequestField = { id: 123 };
      jest.spyOn(vRequestFieldFormService, 'getVRequestField').mockReturnValue({ id: null });
      jest.spyOn(vRequestFieldService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vRequestField: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vRequestField }));
      saveSubject.complete();

      // THEN
      expect(vRequestFieldFormService.getVRequestField).toHaveBeenCalled();
      expect(vRequestFieldService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVRequestField>>();
      const vRequestField = { id: 123 };
      jest.spyOn(vRequestFieldService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vRequestField });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(vRequestFieldService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareVEndpoint', () => {
      it('Should forward to vEndpointService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(vEndpointService, 'compareVEndpoint');
        comp.compareVEndpoint(entity, entity2);
        expect(vEndpointService.compareVEndpoint).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
