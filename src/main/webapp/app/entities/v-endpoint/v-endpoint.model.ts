export interface IVEndpoint {
  id: number;
  code?: string | null;
  name?: string | null;
  description?: string | null;
  url?: string | null;
  method?: string | null;
}

export type NewVEndpoint = Omit<IVEndpoint, 'id'> & { id: null };
