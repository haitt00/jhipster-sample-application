import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IVEndpoint, NewVEndpoint } from '../v-endpoint.model';

export type PartialUpdateVEndpoint = Partial<IVEndpoint> & Pick<IVEndpoint, 'id'>;

export type EntityResponseType = HttpResponse<IVEndpoint>;
export type EntityArrayResponseType = HttpResponse<IVEndpoint[]>;

@Injectable({ providedIn: 'root' })
export class VEndpointService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/v-endpoints');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(vEndpoint: NewVEndpoint): Observable<EntityResponseType> {
    return this.http.post<IVEndpoint>(this.resourceUrl, vEndpoint, { observe: 'response' });
  }

  update(vEndpoint: IVEndpoint): Observable<EntityResponseType> {
    return this.http.put<IVEndpoint>(`${this.resourceUrl}/${this.getVEndpointIdentifier(vEndpoint)}`, vEndpoint, { observe: 'response' });
  }

  partialUpdate(vEndpoint: PartialUpdateVEndpoint): Observable<EntityResponseType> {
    return this.http.patch<IVEndpoint>(`${this.resourceUrl}/${this.getVEndpointIdentifier(vEndpoint)}`, vEndpoint, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IVEndpoint>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IVEndpoint[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getVEndpointIdentifier(vEndpoint: Pick<IVEndpoint, 'id'>): number {
    return vEndpoint.id;
  }

  compareVEndpoint(o1: Pick<IVEndpoint, 'id'> | null, o2: Pick<IVEndpoint, 'id'> | null): boolean {
    return o1 && o2 ? this.getVEndpointIdentifier(o1) === this.getVEndpointIdentifier(o2) : o1 === o2;
  }

  addVEndpointToCollectionIfMissing<Type extends Pick<IVEndpoint, 'id'>>(
    vEndpointCollection: Type[],
    ...vEndpointsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const vEndpoints: Type[] = vEndpointsToCheck.filter(isPresent);
    if (vEndpoints.length > 0) {
      const vEndpointCollectionIdentifiers = vEndpointCollection.map(vEndpointItem => this.getVEndpointIdentifier(vEndpointItem)!);
      const vEndpointsToAdd = vEndpoints.filter(vEndpointItem => {
        const vEndpointIdentifier = this.getVEndpointIdentifier(vEndpointItem);
        if (vEndpointCollectionIdentifiers.includes(vEndpointIdentifier)) {
          return false;
        }
        vEndpointCollectionIdentifiers.push(vEndpointIdentifier);
        return true;
      });
      return [...vEndpointsToAdd, ...vEndpointCollection];
    }
    return vEndpointCollection;
  }
}
