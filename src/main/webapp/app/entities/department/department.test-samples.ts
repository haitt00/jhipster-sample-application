import { IDepartment, NewDepartment } from './department.model';

export const sampleWithRequiredData: IDepartment = {
  id: 19177,
  departmentName: 'Bespoke',
};

export const sampleWithPartialData: IDepartment = {
  id: 15391,
  departmentName: 'Music',
};

export const sampleWithFullData: IDepartment = {
  id: 20598,
  departmentName: 'Global Practical Ford',
};

export const sampleWithNewData: NewDepartment = {
  departmentName: 'Lamborghini override',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
