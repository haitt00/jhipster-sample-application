import { IVEndpoint, NewVEndpoint } from './v-endpoint.model';

export const sampleWithRequiredData: IVEndpoint = {
  id: 51760,
};

export const sampleWithPartialData: IVEndpoint = {
  id: 54802,
  method: 'Data',
};

export const sampleWithFullData: IVEndpoint = {
  id: 87021,
  code: 'Rubber',
  name: 'Montana Shoes',
  description: 'panel',
  url: 'https://frank.info',
  method: 'Small',
};

export const sampleWithNewData: NewVEndpoint = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
