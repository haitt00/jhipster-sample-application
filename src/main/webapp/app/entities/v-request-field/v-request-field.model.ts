import { IVEndpoint } from 'app/entities/v-endpoint/v-endpoint.model';

export interface IVRequestField {
  id: number;
  endpointId?: number | null;
  code?: string | null;
  name?: string | null;
  vEndpoint?: Pick<IVEndpoint, 'id'> | null;
}

export type NewVRequestField = Omit<IVRequestField, 'id'> & { id: null };
