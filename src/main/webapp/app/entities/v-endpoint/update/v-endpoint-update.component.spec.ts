import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { VEndpointFormService } from './v-endpoint-form.service';
import { VEndpointService } from '../service/v-endpoint.service';
import { IVEndpoint } from '../v-endpoint.model';

import { VEndpointUpdateComponent } from './v-endpoint-update.component';

describe('VEndpoint Management Update Component', () => {
  let comp: VEndpointUpdateComponent;
  let fixture: ComponentFixture<VEndpointUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let vEndpointFormService: VEndpointFormService;
  let vEndpointService: VEndpointService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [VEndpointUpdateComponent],
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
      .overrideTemplate(VEndpointUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(VEndpointUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    vEndpointFormService = TestBed.inject(VEndpointFormService);
    vEndpointService = TestBed.inject(VEndpointService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const vEndpoint: IVEndpoint = { id: 456 };

      activatedRoute.data = of({ vEndpoint });
      comp.ngOnInit();

      expect(comp.vEndpoint).toEqual(vEndpoint);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVEndpoint>>();
      const vEndpoint = { id: 123 };
      jest.spyOn(vEndpointFormService, 'getVEndpoint').mockReturnValue(vEndpoint);
      jest.spyOn(vEndpointService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vEndpoint });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vEndpoint }));
      saveSubject.complete();

      // THEN
      expect(vEndpointFormService.getVEndpoint).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(vEndpointService.update).toHaveBeenCalledWith(expect.objectContaining(vEndpoint));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVEndpoint>>();
      const vEndpoint = { id: 123 };
      jest.spyOn(vEndpointFormService, 'getVEndpoint').mockReturnValue({ id: null });
      jest.spyOn(vEndpointService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vEndpoint: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vEndpoint }));
      saveSubject.complete();

      // THEN
      expect(vEndpointFormService.getVEndpoint).toHaveBeenCalled();
      expect(vEndpointService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVEndpoint>>();
      const vEndpoint = { id: 123 };
      jest.spyOn(vEndpointService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vEndpoint });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(vEndpointService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
