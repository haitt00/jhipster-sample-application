import dayjs from 'dayjs/esm';

import { IEmployee, NewEmployee } from './employee.model';

export const sampleWithRequiredData: IEmployee = {
  id: 6938,
};

export const sampleWithPartialData: IEmployee = {
  id: 25479,
  firstName: 'Felicita',
  email: 'Angelo_Nader@hotmail.com',
  phoneNumber: 'Plastic',
  hireDate: dayjs('2023-08-14T10:13'),
  commissionPct: 3539,
};

export const sampleWithFullData: IEmployee = {
  id: 6020,
  firstName: 'Hildegard',
  lastName: 'Kutch',
  email: 'Ibrahim_Olson86@yahoo.com',
  phoneNumber: 'synthesize behind',
  hireDate: dayjs('2023-08-15T05:28'),
  salary: 4201,
  commissionPct: 4837,
};

export const sampleWithNewData: NewEmployee = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
