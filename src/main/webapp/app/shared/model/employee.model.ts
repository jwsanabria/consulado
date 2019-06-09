import { Moment } from 'moment';
import { IJob } from 'app/shared/model/job.model';
import { IEmployee } from 'app/shared/model/employee.model';
import { IKanbam } from 'app/shared/model/kanbam.model';

export interface IEmployee {
  id?: number;
  firstName?: string;
  lastName?: string;
  email?: string;
  phoneNumber?: string;
  hireDate?: Moment;
  salary?: number;
  commissionPct?: number;
  jobs?: IJob[];
  manager?: IEmployee;
  kanbam?: IKanbam;
}

export const defaultValue: Readonly<IEmployee> = {};
