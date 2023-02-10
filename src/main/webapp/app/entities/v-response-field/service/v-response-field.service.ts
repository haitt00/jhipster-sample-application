import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IVResponseField, NewVResponseField } from '../v-response-field.model';

export type PartialUpdateVResponseField = Partial<IVResponseField> & Pick<IVResponseField, 'id'>;

export type EntityResponseType = HttpResponse<IVResponseField>;
export type EntityArrayResponseType = HttpResponse<IVResponseField[]>;

@Injectable({ providedIn: 'root' })
export class VResponseFieldService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/v-response-fields');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(vResponseField: NewVResponseField): Observable<EntityResponseType> {
    return this.http.post<IVResponseField>(this.resourceUrl, vResponseField, { observe: 'response' });
  }

  update(vResponseField: IVResponseField): Observable<EntityResponseType> {
    return this.http.put<IVResponseField>(`${this.resourceUrl}/${this.getVResponseFieldIdentifier(vResponseField)}`, vResponseField, {
      observe: 'response',
    });
  }

  partialUpdate(vResponseField: PartialUpdateVResponseField): Observable<EntityResponseType> {
    return this.http.patch<IVResponseField>(`${this.resourceUrl}/${this.getVResponseFieldIdentifier(vResponseField)}`, vResponseField, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IVResponseField>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IVResponseField[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getVResponseFieldIdentifier(vResponseField: Pick<IVResponseField, 'id'>): number {
    return vResponseField.id;
  }

  compareVResponseField(o1: Pick<IVResponseField, 'id'> | null, o2: Pick<IVResponseField, 'id'> | null): boolean {
    return o1 && o2 ? this.getVResponseFieldIdentifier(o1) === this.getVResponseFieldIdentifier(o2) : o1 === o2;
  }

  addVResponseFieldToCollectionIfMissing<Type extends Pick<IVResponseField, 'id'>>(
    vResponseFieldCollection: Type[],
    ...vResponseFieldsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const vResponseFields: Type[] = vResponseFieldsToCheck.filter(isPresent);
    if (vResponseFields.length > 0) {
      const vResponseFieldCollectionIdentifiers = vResponseFieldCollection.map(
        vResponseFieldItem => this.getVResponseFieldIdentifier(vResponseFieldItem)!
      );
      const vResponseFieldsToAdd = vResponseFields.filter(vResponseFieldItem => {
        const vResponseFieldIdentifier = this.getVResponseFieldIdentifier(vResponseFieldItem);
        if (vResponseFieldCollectionIdentifiers.includes(vResponseFieldIdentifier)) {
          return false;
        }
        vResponseFieldCollectionIdentifiers.push(vResponseFieldIdentifier);
        return true;
      });
      return [...vResponseFieldsToAdd, ...vResponseFieldCollection];
    }
    return vResponseFieldCollection;
  }
}
