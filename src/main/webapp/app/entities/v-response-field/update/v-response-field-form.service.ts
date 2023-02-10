import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IVResponseField, NewVResponseField } from '../v-response-field.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IVResponseField for edit and NewVResponseFieldFormGroupInput for create.
 */
type VResponseFieldFormGroupInput = IVResponseField | PartialWithRequiredKeyOf<NewVResponseField>;

type VResponseFieldFormDefaults = Pick<NewVResponseField, 'id'>;

type VResponseFieldFormGroupContent = {
  id: FormControl<IVResponseField['id'] | NewVResponseField['id']>;
  endpointId: FormControl<IVResponseField['endpointId']>;
  code: FormControl<IVResponseField['code']>;
  name: FormControl<IVResponseField['name']>;
  vEndpoint: FormControl<IVResponseField['vEndpoint']>;
};

export type VResponseFieldFormGroup = FormGroup<VResponseFieldFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class VResponseFieldFormService {
  createVResponseFieldFormGroup(vResponseField: VResponseFieldFormGroupInput = { id: null }): VResponseFieldFormGroup {
    const vResponseFieldRawValue = {
      ...this.getFormDefaults(),
      ...vResponseField,
    };
    return new FormGroup<VResponseFieldFormGroupContent>({
      id: new FormControl(
        { value: vResponseFieldRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      endpointId: new FormControl(vResponseFieldRawValue.endpointId),
      code: new FormControl(vResponseFieldRawValue.code),
      name: new FormControl(vResponseFieldRawValue.name),
      vEndpoint: new FormControl(vResponseFieldRawValue.vEndpoint),
    });
  }

  getVResponseField(form: VResponseFieldFormGroup): IVResponseField | NewVResponseField {
    return form.getRawValue() as IVResponseField | NewVResponseField;
  }

  resetForm(form: VResponseFieldFormGroup, vResponseField: VResponseFieldFormGroupInput): void {
    const vResponseFieldRawValue = { ...this.getFormDefaults(), ...vResponseField };
    form.reset(
      {
        ...vResponseFieldRawValue,
        id: { value: vResponseFieldRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): VResponseFieldFormDefaults {
    return {
      id: null,
    };
  }
}
