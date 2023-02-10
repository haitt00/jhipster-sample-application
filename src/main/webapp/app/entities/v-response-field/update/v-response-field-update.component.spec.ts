import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { VResponseFieldFormService } from './v-response-field-form.service';
import { VResponseFieldService } from '../service/v-response-field.service';
import { IVResponseField } from '../v-response-field.model';
import { IVEndpoint } from 'app/entities/v-endpoint/v-endpoint.model';
import { VEndpointService } from 'app/entities/v-endpoint/service/v-endpoint.service';

import { VResponseFieldUpdateComponent } from './v-response-field-update.component';

describe('VResponseField Management Update Component', () => {
  let comp: VResponseFieldUpdateComponent;
  let fixture: ComponentFixture<VResponseFieldUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let vResponseFieldFormService: VResponseFieldFormService;
  let vResponseFieldService: VResponseFieldService;
  let vEndpointService: VEndpointService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [VResponseFieldUpdateComponent],
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
      .overrideTemplate(VResponseFieldUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(VResponseFieldUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    vResponseFieldFormService = TestBed.inject(VResponseFieldFormService);
    vResponseFieldService = TestBed.inject(VResponseFieldService);
    vEndpointService = TestBed.inject(VEndpointService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call VEndpoint query and add missing value', () => {
      const vResponseField: IVResponseField = { id: 456 };
      const vEndpoint: IVEndpoint = { id: 53175 };
      vResponseField.vEndpoint = vEndpoint;

      const vEndpointCollection: IVEndpoint[] = [{ id: 23945 }];
      jest.spyOn(vEndpointService, 'query').mockReturnValue(of(new HttpResponse({ body: vEndpointCollection })));
      const additionalVEndpoints = [vEndpoint];
      const expectedCollection: IVEndpoint[] = [...additionalVEndpoints, ...vEndpointCollection];
      jest.spyOn(vEndpointService, 'addVEndpointToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ vResponseField });
      comp.ngOnInit();

      expect(vEndpointService.query).toHaveBeenCalled();
      expect(vEndpointService.addVEndpointToCollectionIfMissing).toHaveBeenCalledWith(
        vEndpointCollection,
        ...additionalVEndpoints.map(expect.objectContaining)
      );
      expect(comp.vEndpointsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const vResponseField: IVResponseField = { id: 456 };
      const vEndpoint: IVEndpoint = { id: 5980 };
      vResponseField.vEndpoint = vEndpoint;

      activatedRoute.data = of({ vResponseField });
      comp.ngOnInit();

      expect(comp.vEndpointsSharedCollection).toContain(vEndpoint);
      expect(comp.vResponseField).toEqual(vResponseField);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVResponseField>>();
      const vResponseField = { id: 123 };
      jest.spyOn(vResponseFieldFormService, 'getVResponseField').mockReturnValue(vResponseField);
      jest.spyOn(vResponseFieldService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vResponseField });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vResponseField }));
      saveSubject.complete();

      // THEN
      expect(vResponseFieldFormService.getVResponseField).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(vResponseFieldService.update).toHaveBeenCalledWith(expect.objectContaining(vResponseField));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVResponseField>>();
      const vResponseField = { id: 123 };
      jest.spyOn(vResponseFieldFormService, 'getVResponseField').mockReturnValue({ id: null });
      jest.spyOn(vResponseFieldService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vResponseField: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vResponseField }));
      saveSubject.complete();

      // THEN
      expect(vResponseFieldFormService.getVResponseField).toHaveBeenCalled();
      expect(vResponseFieldService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVResponseField>>();
      const vResponseField = { id: 123 };
      jest.spyOn(vResponseFieldService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vResponseField });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(vResponseFieldService.update).toHaveBeenCalled();
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
