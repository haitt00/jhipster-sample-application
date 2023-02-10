import { IVResponseField, NewVResponseField } from './v-response-field.model';

export const sampleWithRequiredData: IVResponseField = {
  id: 14056,
};

export const sampleWithPartialData: IVResponseField = {
  id: 85945,
  endpointId: 97022,
  code: 'global',
  name: 'Money Nevada',
};

export const sampleWithFullData: IVResponseField = {
  id: 67143,
  endpointId: 28246,
  code: 'Internal gold Bedfordshire',
  name: 'Auto',
};

export const sampleWithNewData: NewVResponseField = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
