import { ICreditCard, NewCreditCard } from './credit-card.model';

export const sampleWithRequiredData: ICreditCard = {
  id: 23056,
};

export const sampleWithPartialData: ICreditCard = {
  id: 68437,
  bank: 'Finland Interactions',
};

export const sampleWithFullData: ICreditCard = {
  id: 50765,
  number: 'up',
  bank: 'Account',
};

export const sampleWithNewData: NewCreditCard = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
