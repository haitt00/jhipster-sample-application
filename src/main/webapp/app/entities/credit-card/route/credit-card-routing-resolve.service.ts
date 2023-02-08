import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICreditCard } from '../credit-card.model';
import { CreditCardService } from '../service/credit-card.service';

@Injectable({ providedIn: 'root' })
export class CreditCardRoutingResolveService implements Resolve<ICreditCard | null> {
  constructor(protected service: CreditCardService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICreditCard | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((creditCard: HttpResponse<ICreditCard>) => {
          if (creditCard.body) {
            return of(creditCard.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
