import { IJob } from 'app/shared/model/job.model';
import { IKanbam } from 'app/shared/model/kanbam.model';

export interface ITask {
  id?: number;
  title?: string;
  description?: string;
  jobs?: IJob[];
  kanbam?: IKanbam;
}

export const defaultValue: Readonly<ITask> = {};
