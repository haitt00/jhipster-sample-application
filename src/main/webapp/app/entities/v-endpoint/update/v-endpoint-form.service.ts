import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IVEndpoint, NewVEndpoint } from '../v-endpoint.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IVEndpoint for edit and NewVEndpointFormGroupInput for create.
 */
type VEndpointFormGroupInput = IVEndpoint | PartialWithRequiredKeyOf<NewVEndpoint>;

type VEndpointFormDefaults = Pick<NewVEndpoint, 'id'>;

type VEndpointFormGroupContent = {
  id: FormControl<IVEndpoint['id'] | NewVEndpoint['id']>;
  code: FormControl<IVEndpoint['code']>;
  name: FormControl<IVEndpoint['name']>;
  description: FormControl<IVEndpoint['description']>;
  url: FormControl<IVEndpoint['url']>;
  method: FormControl<IVEndpoint['method']>;
};

export type VEndpointFormGroup = FormGroup<VEndpointFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class VEndpointFormService {
  createVEndpointFormGroup(vEndpoint: VEndpointFormGroupInput = { id: null }): VEndpointFormGroup {
    const vEndpointRawValue = {
      ...this.getFormDefaults(),
      ...vEndpoint,
    };
    return new FormGroup<VEndpointFormGroupContent>({
      id: new FormControl(
        { value: vEndpointRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      code: new FormControl(vEndpointRawValue.code),
      name: new FormControl(vEndpointRawValue.name),
      description: new FormControl(vEndpointRawValue.description),
      url: new FormControl(vEndpointRawValue.url),
      method: new FormControl(vEndpointRawValue.method),
    });
  }

  getVEndpoint(form: VEndpointFormGroup): IVEndpoint | NewVEndpoint {
    return form.getRawValue() as IVEndpoint | NewVEndpoint;
  }

  resetForm(form: VEndpointFormGroup, vEndpoint: VEndpointFormGroupInput): void {
    const vEndpointRawValue = { ...this.getFormDefaults(), ...vEndpoint };
    form.reset(
      {
        ...vEndpointRawValue,
        id: { value: vEndpointRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): VEndpointFormDefaults {
    return {
      id: null,
    };
  }
}
