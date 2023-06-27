import { Component, Output,EventEmitter } from '@angular/core';
import { GroupService } from '../services/group.service';
import { GroupListComponent } from '../group-list/group-list.component';
import { Group } from '../model/group.model';
import { HttpClient } from '@angular/common/http';
import { AuthService } from '../services/auth.service';
import { DatePipe } from '@angular/common';


@Component({
  selector: 'app-new-group-popup',
  templateUrl: './new-group-popup.component.html',
  styleUrls: ['./new-group-popup.component.css']
})
export class NewGroupPopupComponent {
  groupName: string;
  groupDescription: string;
  groupCreationDate: Date;
  groupIsSuspended: boolean;
  groupSuspendedReason: string;
  groupIsDeleted: boolean;
  groups: Group[] = [];


  @Output() groupAdded = new EventEmitter<Group>();
  @Output() popupClosed = new EventEmitter();

  addGroup() {
    const newGroup: Group = {
      name: this.groupName,
      description: this.groupDescription,
      isSuspended: this.groupIsSuspended,
      suspendedReason: this.groupSuspendedReason,
      isDeleted: this.groupIsDeleted
    };
  
    const headers = this.authService.getAuthenticatedHeaders();
  
    this.http.post<Group>('http://localhost:8080/groups', newGroup, { headers })
      .subscribe(
        (response) => {
          console.log('Grupa uspešno dodata:', response);
          this.groupAdded.emit(response);
          this.closePopup();
        },
        (error) => {
          console.error('Greška prilikom dodavanja grupe:', error);
        }
      );
  }
   

  closePopup() {
    this.popupClosed.emit();
  }

  constructor(private http: HttpClient, private groupService: GroupService, private authService: AuthService, private datePipe: DatePipe){
    const currentDate = new Date();

    this.groupName = '';
    this.groupDescription = '';
    this.groupCreationDate = currentDate;
    this.groupIsSuspended = false;
    this.groupSuspendedReason = '';
    this.groupIsDeleted = false;

    this.loadGroups();
  }

  loadGroups() {
    this.groupService.getAllGroups().subscribe(
      groups => {
        this.groups = groups;
        console.log(this.groups);
      },
      error => {
        console.error('Greška prilikom preuzimanja grupa:', error);
      }
    );
  }
  
  
}
