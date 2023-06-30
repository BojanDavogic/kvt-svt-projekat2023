import { Component, Output,EventEmitter, Input } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { GroupService } from '../services/group.service';

@Component({
  selector: 'app-edit-group-popup',
  templateUrl: './edit-group-popup.component.html',
  styleUrls: ['./edit-group-popup.component.css']
})
export class EditGroupPopupComponent {
  // groupName: string;
  // groupDescription: string;
  // groupCreationDate: Date;
  // groupIsSuspended: boolean;
  // groupSuspendedReason: string;
  // groupIsDeleted: boolean;

  @Input() group: any;

  @Output() groupEdited = new EventEmitter();
  @Output() popupEditClosed = new EventEmitter();

  showEditPopup: boolean = false;

  constructor(private route: ActivatedRoute, private groupService: GroupService) { }

  updateGroup() {
    this.groupService.updateGroup(this.group.id, this.group).subscribe(
      (response) => {
        console.log('Grupa uspešno izmenjena:', response);
        // Emitujte događaj da je grupa izmenjena
        // this.groupEdited.emit(response);
        // Zatvorite popup
        this.closeEditPopup();
      },
      (error) => {
        console.error('Greška prilikom izmene grupe:', error);
      }
    );
  }

  closeEditPopup() {
    this.popupEditClosed.emit();

  }
}
