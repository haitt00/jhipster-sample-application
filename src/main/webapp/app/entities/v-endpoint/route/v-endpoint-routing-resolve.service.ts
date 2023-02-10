import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IVEndpoint } from '../v-endpoint.model';
import { VEndpointService } from '../service/v-endpoint.service';

@Injectable({ providedIn: 'root' })
export class VEndpointRoutingResolveService implements Resolve<IVEndpoint | null> {
  constructor(protected service: VEndpointService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IVEndpoint | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((vEndpoint: HttpResponse<IVEndpoint>) => {
          if (vEndpoint.body) {
            return of(vEndpoint.body);
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
