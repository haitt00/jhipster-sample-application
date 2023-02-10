import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IVResponseField } from '../v-response-field.model';
import { VResponseFieldService } from '../service/v-response-field.service';

@Injectable({ providedIn: 'root' })
export class VResponseFieldRoutingResolveService implements Resolve<IVResponseField | null> {
  constructor(protected service: VResponseFieldService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IVResponseField | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((vResponseField: HttpResponse<IVResponseField>) => {
          if (vResponseField.body) {
            return of(vResponseField.body);
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
