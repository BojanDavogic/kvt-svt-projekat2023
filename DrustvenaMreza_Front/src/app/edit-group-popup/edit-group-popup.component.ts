import { Component, Output,EventEmitter, Input } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { GroupService } from '../services/group.service';
import { Group } from '../model/group.model';

@Component({
  selector: 'app-edit-group-popup',
  templateUrl: './edit-group-popup.component.html',
  styleUrls: ['./edit-group-popup.component.css']
})
export class EditGroupPopupComponent {

  @Input() group: Group | undefined;

  @Output() groupEdited = new EventEmitter();
  @Output() popupEditClosed = new EventEmitter();

  showEditPopup: boolean = false;

  constructor(private route: ActivatedRoute, private groupService: GroupService) { }

  updateGroup() {
    if(this.group){
      this.groupService.updateGroup(this.group.id, this.group).subscribe(
        (response) => {
          console.log('Grupa uspešno izmenjena:', response);
          this.closeEditPopup();
        },
        (error) => {
          console.error('Greška prilikom izmene grupe:', error);
        }
      );
    }
  }

  closeEditPopup() {
    this.popupEditClosed.emit();

  }
}
