import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Group } from '../model/group.model';
import { GroupService } from '../services/group.service';

@Component({
  selector: 'app-add-rules',
  templateUrl: './add-rules.component.html',
  styleUrls: ['./add-rules.component.css']
})
export class AddRulesComponent {
  groupRules: string = '';

  @Input() group: Group | undefined;

  @Output() rulesAdded = new EventEmitter<Group>();
  @Output() popupClosed = new EventEmitter<void>();

  addRules() {

    const newGroup: any = {
      rules: this.groupRules
    };
    if(this.group){
    this.groupService.updateGroupRules(this.group.id, this.groupRules)
      .subscribe(
        (response) => {
          this.rulesAdded.emit(response);
          this.closePopup();
        },
        (error) => {
          console.error('Greška prilikom dodavanja pravila grupe:', error);
        }
      );
    }
  }

  closePopup() {
    this.popupClosed.emit();
  }

  constructor(private groupService: GroupService) {
    const currentDate = new Date();

    this.groupRules = '';
  }

}
