import { IJob, NewJob } from './job.model';

export const sampleWithRequiredData: IJob = {
  id: 10784,
};

export const sampleWithPartialData: IJob = {
  id: 12812,
};

export const sampleWithFullData: IJob = {
  id: 28262,
  jobTitle: 'Senior Solutions Liaison',
  minSalary: 21833,
  maxSalary: 30099,
};

export const sampleWithNewData: NewJob = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
