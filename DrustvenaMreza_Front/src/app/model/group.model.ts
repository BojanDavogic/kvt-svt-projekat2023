import { Moment } from "moment";

export interface Group {
  id?: any;
  name: string;
  description: string;
  creationDate: Date;
  isSuspended: boolean;
  suspendedReason: string;
  isDeleted: boolean;
  }