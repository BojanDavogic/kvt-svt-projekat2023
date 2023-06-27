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

  // @Output() groupAdded = new EventEmitter<Group>();
  @Output() popupClosed = new EventEmitter();

  constructor(private route: ActivatedRoute, private groupService: GroupService) { }

  updateGroup() {
    this.groupService.updateGroup(this.group.id, this.group).subscribe(
      (response) => {
        console.log('Grupa uspešno izmenjena:', response);
        // Emitujte događaj da je grupa izmenjena
        // this.groupAdded.emit(response);
        // Zatvorite popup
        this.closePopup();
      },
      (error) => {
        console.error('Greška prilikom izmene grupe:', error);
      }
    );
  }

  closePopup() {
    this.popupClosed.emit();
  }
}
