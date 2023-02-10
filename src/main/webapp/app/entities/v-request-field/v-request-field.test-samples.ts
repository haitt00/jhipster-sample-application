import { IVRequestField, NewVRequestField } from './v-request-field.model';

export const sampleWithRequiredData: IVRequestField = {
  id: 97390,
};

export const sampleWithPartialData: IVRequestField = {
  id: 32672,
  endpointId: 90290,
  code: 'Granite forecast Directives',
  name: 'Markets',
};

export const sampleWithFullData: IVRequestField = {
  id: 74185,
  endpointId: 23961,
  code: 'Car Savings Hungary',
  name: 'composite',
};

export const sampleWithNewData: NewVRequestField = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
