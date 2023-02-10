import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../v-endpoint.test-samples';

import { VEndpointFormService } from './v-endpoint-form.service';

describe('VEndpoint Form Service', () => {
  let service: VEndpointFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(VEndpointFormService);
  });

  describe('Service methods', () => {
    describe('createVEndpointFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createVEndpointFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            url: expect.any(Object),
            method: expect.any(Object),
          })
        );
      });

      it('passing IVEndpoint should create a new form with FormGroup', () => {
        const formGroup = service.createVEndpointFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            name: expect.any(Object),
            description: expect.any(Object),
            url: expect.any(Object),
            method: expect.any(Object),
          })
        );
      });
    });

    describe('getVEndpoint', () => {
      it('should return NewVEndpoint for default VEndpoint initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createVEndpointFormGroup(sampleWithNewData);

        const vEndpoint = service.getVEndpoint(formGroup) as any;

        expect(vEndpoint).toMatchObject(sampleWithNewData);
      });

      it('should return NewVEndpoint for empty VEndpoint initial value', () => {
        const formGroup = service.createVEndpointFormGroup();

        const vEndpoint = service.getVEndpoint(formGroup) as any;

        expect(vEndpoint).toMatchObject({});
      });

      it('should return IVEndpoint', () => {
        const formGroup = service.createVEndpointFormGroup(sampleWithRequiredData);

        const vEndpoint = service.getVEndpoint(formGroup) as any;

        expect(vEndpoint).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IVEndpoint should not enable id FormControl', () => {
        const formGroup = service.createVEndpointFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewVEndpoint should disable id FormControl', () => {
        const formGroup = service.createVEndpointFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
