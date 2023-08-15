import { ITask, NewTask } from './task.model';

export const sampleWithRequiredData: ITask = {
  id: 3350,
};

export const sampleWithPartialData: ITask = {
  id: 13826,
  description: 'separately markets Configurable',
};

export const sampleWithFullData: ITask = {
  id: 14798,
  title: 'port interface',
  description: 'desolate Fresh',
};

export const sampleWithNewData: NewTask = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
