import { Component, Output, EventEmitter } from '@angular/core';
import { GroupService } from '../services/group.service';
import { Group } from '../model/group.model';
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
  selectedFile: File | null = null;

  @Output() groupAdded = new EventEmitter<Group>();
  @Output() popupClosed = new EventEmitter();

  onFileSelected(event: any) {
    this.selectedFile = event.target.files[0];
  }

  addGroup() {
    if (!this.selectedFile) {
      alert('Morate odabrati PDF fajl.');
      return;
    }

    const newGroup: Group = {
      name: this.groupName,
      description: this.groupDescription,
      isSuspended: this.groupIsSuspended,
      suspendedReason: this.groupSuspendedReason,
      isDeleted: this.groupIsDeleted
    };

    this.groupService.createGroup(newGroup, this.selectedFile)
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

  constructor(private groupService: GroupService, private authService: AuthService, private datePipe: DatePipe) {
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
