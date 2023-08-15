import dayjs from 'dayjs/esm';

import { Language } from 'app/entities/enumerations/language.model';

import { IJobHistory, NewJobHistory } from './job-history.model';

export const sampleWithRequiredData: IJobHistory = {
  id: 22644,
};

export const sampleWithPartialData: IJobHistory = {
  id: 18206,
  startDate: dayjs('2023-08-14T23:05'),
  endDate: dayjs('2023-08-15T02:20'),
};

export const sampleWithFullData: IJobHistory = {
  id: 29910,
  startDate: dayjs('2023-08-15T06:44'),
  endDate: dayjs('2023-08-14T10:49'),
  language: 'SPANISH',
};

export const sampleWithNewData: NewJobHistory = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
