export interface ICustomer {
  id: number;
  name?: string | null;
  phone?: string | null;
  mail?: string | null;
}

export type NewCustomer = Omit<ICustomer, 'id'> & { id: null };
