import { IRegion, NewRegion } from './region.model';

export const sampleWithRequiredData: IRegion = {
  id: 8248,
};

export const sampleWithPartialData: IRegion = {
  id: 12729,
  regionName: 'deposit Cisgender',
};

export const sampleWithFullData: IRegion = {
  id: 27401,
  regionName: 'Orchard aut',
};

export const sampleWithNewData: NewRegion = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
