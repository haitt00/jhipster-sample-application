import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICreditCard, NewCreditCard } from '../credit-card.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICreditCard for edit and NewCreditCardFormGroupInput for create.
 */
type CreditCardFormGroupInput = ICreditCard | PartialWithRequiredKeyOf<NewCreditCard>;

type CreditCardFormDefaults = Pick<NewCreditCard, 'id'>;

type CreditCardFormGroupContent = {
  id: FormControl<ICreditCard['id'] | NewCreditCard['id']>;
  number: FormControl<ICreditCard['number']>;
  bank: FormControl<ICreditCard['bank']>;
  owner: FormControl<ICreditCard['owner']>;
};

export type CreditCardFormGroup = FormGroup<CreditCardFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CreditCardFormService {
  createCreditCardFormGroup(creditCard: CreditCardFormGroupInput = { id: null }): CreditCardFormGroup {
    const creditCardRawValue = {
      ...this.getFormDefaults(),
      ...creditCard,
    };
    return new FormGroup<CreditCardFormGroupContent>({
      id: new FormControl(
        { value: creditCardRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      number: new FormControl(creditCardRawValue.number),
      bank: new FormControl(creditCardRawValue.bank),
      owner: new FormControl(creditCardRawValue.owner),
    });
  }

  getCreditCard(form: CreditCardFormGroup): ICreditCard | NewCreditCard {
    return form.getRawValue() as ICreditCard | NewCreditCard;
  }

  resetForm(form: CreditCardFormGroup, creditCard: CreditCardFormGroupInput): void {
    const creditCardRawValue = { ...this.getFormDefaults(), ...creditCard };
    form.reset(
      {
        ...creditCardRawValue,
        id: { value: creditCardRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): CreditCardFormDefaults {
    return {
      id: null,
    };
  }
}
