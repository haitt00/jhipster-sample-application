import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CreditCardFormService } from './credit-card-form.service';
import { CreditCardService } from '../service/credit-card.service';
import { ICreditCard } from '../credit-card.model';
import { ICustomer } from 'app/entities/customer/customer.model';
import { CustomerService } from 'app/entities/customer/service/customer.service';

import { CreditCardUpdateComponent } from './credit-card-update.component';

describe('CreditCard Management Update Component', () => {
  let comp: CreditCardUpdateComponent;
  let fixture: ComponentFixture<CreditCardUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let creditCardFormService: CreditCardFormService;
  let creditCardService: CreditCardService;
  let customerService: CustomerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CreditCardUpdateComponent],
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
      .overrideTemplate(CreditCardUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CreditCardUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    creditCardFormService = TestBed.inject(CreditCardFormService);
    creditCardService = TestBed.inject(CreditCardService);
    customerService = TestBed.inject(CustomerService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Customer query and add missing value', () => {
      const creditCard: ICreditCard = { id: 456 };
      const owner: ICustomer = { id: 95226 };
      creditCard.owner = owner;

      const customerCollection: ICustomer[] = [{ id: 88891 }];
      jest.spyOn(customerService, 'query').mockReturnValue(of(new HttpResponse({ body: customerCollection })));
      const additionalCustomers = [owner];
      const expectedCollection: ICustomer[] = [...additionalCustomers, ...customerCollection];
      jest.spyOn(customerService, 'addCustomerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ creditCard });
      comp.ngOnInit();

      expect(customerService.query).toHaveBeenCalled();
      expect(customerService.addCustomerToCollectionIfMissing).toHaveBeenCalledWith(
        customerCollection,
        ...additionalCustomers.map(expect.objectContaining)
      );
      expect(comp.customersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const creditCard: ICreditCard = { id: 456 };
      const owner: ICustomer = { id: 92708 };
      creditCard.owner = owner;

      activatedRoute.data = of({ creditCard });
      comp.ngOnInit();

      expect(comp.customersSharedCollection).toContain(owner);
      expect(comp.creditCard).toEqual(creditCard);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICreditCard>>();
      const creditCard = { id: 123 };
      jest.spyOn(creditCardFormService, 'getCreditCard').mockReturnValue(creditCard);
      jest.spyOn(creditCardService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ creditCard });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: creditCard }));
      saveSubject.complete();

      // THEN
      expect(creditCardFormService.getCreditCard).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(creditCardService.update).toHaveBeenCalledWith(expect.objectContaining(creditCard));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICreditCard>>();
      const creditCard = { id: 123 };
      jest.spyOn(creditCardFormService, 'getCreditCard').mockReturnValue({ id: null });
      jest.spyOn(creditCardService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ creditCard: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: creditCard }));
      saveSubject.complete();

      // THEN
      expect(creditCardFormService.getCreditCard).toHaveBeenCalled();
      expect(creditCardService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICreditCard>>();
      const creditCard = { id: 123 };
      jest.spyOn(creditCardService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ creditCard });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(creditCardService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCustomer', () => {
      it('Should forward to customerService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(customerService, 'compareCustomer');
        comp.compareCustomer(entity, entity2);
        expect(customerService.compareCustomer).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
