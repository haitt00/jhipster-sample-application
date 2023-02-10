import { IVEndpoint } from 'app/entities/v-endpoint/v-endpoint.model';

export interface IVResponseField {
  id: number;
  endpointId?: number | null;
  code?: string | null;
  name?: string | null;
  vEndpoint?: Pick<IVEndpoint, 'id'> | null;
}

export type NewVResponseField = Omit<IVResponseField, 'id'> & { id: null };
