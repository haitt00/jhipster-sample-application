import { ILocation, NewLocation } from './location.model';

export const sampleWithRequiredData: ILocation = {
  id: 5802,
};

export const sampleWithPartialData: ILocation = {
  id: 9258,
  city: 'West Letahaven',
};

export const sampleWithFullData: ILocation = {
  id: 21418,
  streetAddress: 'impactful fuga',
  postalCode: 'mitten free Hyundai',
  city: 'South Guststad',
  stateProvince: 'Bespoke Southeast contact',
};

export const sampleWithNewData: NewLocation = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
