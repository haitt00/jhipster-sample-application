import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { CreditCardFormService, CreditCardFormGroup } from './credit-card-form.service';
import { ICreditCard } from '../credit-card.model';
import { CreditCardService } from '../service/credit-card.service';
import { ICustomer } from 'app/entities/customer/customer.model';
import { CustomerService } from 'app/entities/customer/service/customer.service';

@Component({
  selector: 'jhi-credit-card-update',
  templateUrl: './credit-card-update.component.html',
})
export class CreditCardUpdateComponent implements OnInit {
  isSaving = false;
  creditCard: ICreditCard | null = null;

  customersSharedCollection: ICustomer[] = [];

  editForm: CreditCardFormGroup = this.creditCardFormService.createCreditCardFormGroup();

  constructor(
    protected creditCardService: CreditCardService,
    protected creditCardFormService: CreditCardFormService,
    protected customerService: CustomerService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareCustomer = (o1: ICustomer | null, o2: ICustomer | null): boolean => this.customerService.compareCustomer(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ creditCard }) => {
      this.creditCard = creditCard;
      if (creditCard) {
        this.updateForm(creditCard);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const creditCard = this.creditCardFormService.getCreditCard(this.editForm);
    if (creditCard.id !== null) {
      this.subscribeToSaveResponse(this.creditCardService.update(creditCard));
    } else {
      this.subscribeToSaveResponse(this.creditCardService.create(creditCard));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICreditCard>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(creditCard: ICreditCard): void {
    this.creditCard = creditCard;
    this.creditCardFormService.resetForm(this.editForm, creditCard);

    this.customersSharedCollection = this.customerService.addCustomerToCollectionIfMissing<ICustomer>(
      this.customersSharedCollection,
      creditCard.owner
    );
  }

  protected loadRelationshipsOptions(): void {
    this.customerService
      .query()
      .pipe(map((res: HttpResponse<ICustomer[]>) => res.body ?? []))
      .pipe(
        map((customers: ICustomer[]) => this.customerService.addCustomerToCollectionIfMissing<ICustomer>(customers, this.creditCard?.owner))
      )
      .subscribe((customers: ICustomer[]) => (this.customersSharedCollection = customers));
  }
}
