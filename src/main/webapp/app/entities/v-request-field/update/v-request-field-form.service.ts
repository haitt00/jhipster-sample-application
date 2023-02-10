import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IVRequestField, NewVRequestField } from '../v-request-field.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IVRequestField for edit and NewVRequestFieldFormGroupInput for create.
 */
type VRequestFieldFormGroupInput = IVRequestField | PartialWithRequiredKeyOf<NewVRequestField>;

type VRequestFieldFormDefaults = Pick<NewVRequestField, 'id'>;

type VRequestFieldFormGroupContent = {
  id: FormControl<IVRequestField['id'] | NewVRequestField['id']>;
  endpointId: FormControl<IVRequestField['endpointId']>;
  code: FormControl<IVRequestField['code']>;
  name: FormControl<IVRequestField['name']>;
  vEndpoint: FormControl<IVRequestField['vEndpoint']>;
};

export type VRequestFieldFormGroup = FormGroup<VRequestFieldFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class VRequestFieldFormService {
  createVRequestFieldFormGroup(vRequestField: VRequestFieldFormGroupInput = { id: null }): VRequestFieldFormGroup {
    const vRequestFieldRawValue = {
      ...this.getFormDefaults(),
      ...vRequestField,
    };
    return new FormGroup<VRequestFieldFormGroupContent>({
      id: new FormControl(
        { value: vRequestFieldRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      endpointId: new FormControl(vRequestFieldRawValue.endpointId),
      code: new FormControl(vRequestFieldRawValue.code),
      name: new FormControl(vRequestFieldRawValue.name),
      vEndpoint: new FormControl(vRequestFieldRawValue.vEndpoint),
    });
  }

  getVRequestField(form: VRequestFieldFormGroup): IVRequestField | NewVRequestField {
    return form.getRawValue() as IVRequestField | NewVRequestField;
  }

  resetForm(form: VRequestFieldFormGroup, vRequestField: VRequestFieldFormGroupInput): void {
    const vRequestFieldRawValue = { ...this.getFormDefaults(), ...vRequestField };
    form.reset(
      {
        ...vRequestFieldRawValue,
        id: { value: vRequestFieldRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): VRequestFieldFormDefaults {
    return {
      id: null,
    };
  }
}
