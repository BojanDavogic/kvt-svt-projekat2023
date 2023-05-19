import { DatePipe } from "@angular/common";

export interface Group {
  id?: any;
  name: string;
  description: string;
  creationDate: Date;
  isSuspended: boolean;
  suspendedReason: string;
  isDeleted: boolean;
  }