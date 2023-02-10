import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../v-response-field.test-samples';

import { VResponseFieldFormService } from './v-response-field-form.service';

describe('VResponseField Form Service', () => {
  let service: VResponseFieldFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(VResponseFieldFormService);
  });

  describe('Service methods', () => {
    describe('createVResponseFieldFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createVResponseFieldFormGroup();

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

      it('passing IVResponseField should create a new form with FormGroup', () => {
        const formGroup = service.createVResponseFieldFormGroup(sampleWithRequiredData);

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

    describe('getVResponseField', () => {
      it('should return NewVResponseField for default VResponseField initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createVResponseFieldFormGroup(sampleWithNewData);

        const vResponseField = service.getVResponseField(formGroup) as any;

        expect(vResponseField).toMatchObject(sampleWithNewData);
      });

      it('should return NewVResponseField for empty VResponseField initial value', () => {
        const formGroup = service.createVResponseFieldFormGroup();

        const vResponseField = service.getVResponseField(formGroup) as any;

        expect(vResponseField).toMatchObject({});
      });

      it('should return IVResponseField', () => {
        const formGroup = service.createVResponseFieldFormGroup(sampleWithRequiredData);

        const vResponseField = service.getVResponseField(formGroup) as any;

        expect(vResponseField).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IVResponseField should not enable id FormControl', () => {
        const formGroup = service.createVResponseFieldFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewVResponseField should disable id FormControl', () => {
        const formGroup = service.createVResponseFieldFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
