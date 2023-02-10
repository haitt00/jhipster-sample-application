import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IVRequestField, NewVRequestField } from '../v-request-field.model';

export type PartialUpdateVRequestField = Partial<IVRequestField> & Pick<IVRequestField, 'id'>;

export type EntityResponseType = HttpResponse<IVRequestField>;
export type EntityArrayResponseType = HttpResponse<IVRequestField[]>;

@Injectable({ providedIn: 'root' })
export class VRequestFieldService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/v-request-fields');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(vRequestField: NewVRequestField): Observable<EntityResponseType> {
    return this.http.post<IVRequestField>(this.resourceUrl, vRequestField, { observe: 'response' });
  }

  update(vRequestField: IVRequestField): Observable<EntityResponseType> {
    return this.http.put<IVRequestField>(`${this.resourceUrl}/${this.getVRequestFieldIdentifier(vRequestField)}`, vRequestField, {
      observe: 'response',
    });
  }

  partialUpdate(vRequestField: PartialUpdateVRequestField): Observable<EntityResponseType> {
    return this.http.patch<IVRequestField>(`${this.resourceUrl}/${this.getVRequestFieldIdentifier(vRequestField)}`, vRequestField, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IVRequestField>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IVRequestField[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getVRequestFieldIdentifier(vRequestField: Pick<IVRequestField, 'id'>): number {
    return vRequestField.id;
  }

  compareVRequestField(o1: Pick<IVRequestField, 'id'> | null, o2: Pick<IVRequestField, 'id'> | null): boolean {
    return o1 && o2 ? this.getVRequestFieldIdentifier(o1) === this.getVRequestFieldIdentifier(o2) : o1 === o2;
  }

  addVRequestFieldToCollectionIfMissing<Type extends Pick<IVRequestField, 'id'>>(
    vRequestFieldCollection: Type[],
    ...vRequestFieldsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const vRequestFields: Type[] = vRequestFieldsToCheck.filter(isPresent);
    if (vRequestFields.length > 0) {
      const vRequestFieldCollectionIdentifiers = vRequestFieldCollection.map(
        vRequestFieldItem => this.getVRequestFieldIdentifier(vRequestFieldItem)!
      );
      const vRequestFieldsToAdd = vRequestFields.filter(vRequestFieldItem => {
        const vRequestFieldIdentifier = this.getVRequestFieldIdentifier(vRequestFieldItem);
        if (vRequestFieldCollectionIdentifiers.includes(vRequestFieldIdentifier)) {
          return false;
        }
        vRequestFieldCollectionIdentifiers.push(vRequestFieldIdentifier);
        return true;
      });
      return [...vRequestFieldsToAdd, ...vRequestFieldCollection];
    }
    return vRequestFieldCollection;
  }
}
