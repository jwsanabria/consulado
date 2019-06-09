import { IEmployee } from 'app/shared/model/employee.model';
import { ITask } from 'app/shared/model/task.model';

export const enum State {
  TO_DO = 'TO_DO',
  DOING = 'DOING',
  DONE = 'DONE'
}

export interface IKanbam {
  id?: number;
  size?: number;
  state?: State;
  owners?: IEmployee[];
  tasks?: ITask[];
}

export const defaultValue: Readonly<IKanbam> = {};
