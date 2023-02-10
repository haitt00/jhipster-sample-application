import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../v-request-field.test-samples';

import { VRequestFieldFormService } from './v-request-field-form.service';

describe('VRequestField Form Service', () => {
  let service: VRequestFieldFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(VRequestFieldFormService);
  });

  describe('Service methods', () => {
    describe('createVRequestFieldFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createVRequestFieldFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            endpointId: expect.any(Object),
            code: expect.any(Object),
            name: expect.any(Object),
            vEndpoint: expect.any(Object),
          })
        );
      });

      it('passing IVRequestField should create a new form with FormGroup', () => {
        const formGroup = service.createVRequestFieldFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            endpointId: expect.any(Object),
            code: expect.any(Object),
            name: expect.any(Object),
            vEndpoint: expect.any(Object),
          })
        );
      });
    });

    describe('getVRequestField', () => {
      it('should return NewVRequestField for default VRequestField initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createVRequestFieldFormGroup(sampleWithNewData);

        const vRequestField = service.getVRequestField(formGroup) as any;

        expect(vRequestField).toMatchObject(sampleWithNewData);
      });

      it('should return NewVRequestField for empty VRequestField initial value', () => {
        const formGroup = service.createVRequestFieldFormGroup();

        const vRequestField = service.getVRequestField(formGroup) as any;

        expect(vRequestField).toMatchObject({});
      });

      it('should return IVRequestField', () => {
        const formGroup = service.createVRequestFieldFormGroup(sampleWithRequiredData);

        const vRequestField = service.getVRequestField(formGroup) as any;

        expect(vRequestField).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IVRequestField should not enable id FormControl', () => {
        const formGroup = service.createVRequestFieldFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewVRequestField should disable id FormControl', () => {
        const formGroup = service.createVRequestFieldFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
