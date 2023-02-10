import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IVRequestField } from '../v-request-field.model';
import { VRequestFieldService } from '../service/v-request-field.service';

@Injectable({ providedIn: 'root' })
export class VRequestFieldRoutingResolveService implements Resolve<IVRequestField | null> {
  constructor(protected service: VRequestFieldService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IVRequestField | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((vRequestField: HttpResponse<IVRequestField>) => {
          if (vRequestField.body) {
            return of(vRequestField.body);
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
