import { ICustomer, NewCustomer } from './customer.model';

export const sampleWithRequiredData: ICustomer = {
  id: 24379,
};

export const sampleWithPartialData: ICustomer = {
  id: 25384,
  phone: '968.502.8755',
  mail: 'Global Kentucky',
};

export const sampleWithFullData: ICustomer = {
  id: 7870,
  name: 'Agent Cotton',
  phone: '752-477-6635 x5053',
  mail: 'multi-byte Philippines',
};

export const sampleWithNewData: NewCustomer = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
