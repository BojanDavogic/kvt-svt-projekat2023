import { group } from "@angular/animations";
import { DatePipe } from "@angular/common";
import { Post } from "./post.model";

export interface Group {
  id?: any;
  name: string;
  description: string;
  creationDate?: Date;
  isSuspended: boolean;
  suspendedReason: string;
  isDeleted: boolean;
}


