import { ICountry, NewCountry } from './country.model';

export const sampleWithRequiredData: ICountry = {
  id: 26138,
};

export const sampleWithPartialData: ICountry = {
  id: 17542,
};

export const sampleWithFullData: ICountry = {
  id: 30066,
  countryName: 'calculating',
};

export const sampleWithNewData: NewCountry = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
